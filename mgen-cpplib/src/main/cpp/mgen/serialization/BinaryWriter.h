/*
 * BinaryWriter.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_MGENBINARYWRITER_H_
#define MGEN_MGENBINARYWRITER_H_

#include "mgen/serialization/VarInt.h"
#include "mgen/serialization/BinaryTags.h"
#include "mgen/util/missingfields.h"

namespace mgen {

template<typename MGenStreamType, typename ClassRegistryType>
class BinaryWriter {
public:

    static const bool default_compact = false;

    BinaryWriter(MGenStreamType& outputStream, const ClassRegistryType& classRegistry, const bool compact =
            default_compact) :
                    m_compact(compact),
                    m_expectType(-1),
                    m_outputStream(outputStream),
                    m_classRegistry(classRegistry) {
    }

    void writeObject(const MGenBase& object) {
        m_expectType = -1;
        writePoly(object, true);
    }

    template<typename T>
    void visit(const T& v, const Field& field) {
        writeFieldStart(field.id(), BINARY_TAG_OF(&v));
        write(v, false);
    }

    template<typename MGenType>
    void beginVisit(const MGenType& object, const int nFieldsToVisit) {

        missingfields::ensureNoMissingFields(object);

        if (shouldOmitIds(MGenType::_type_id)) {
            writeSize((nFieldsToVisit << 2) | 0x02);
        } else {
            const std::vector<short>& ids = MGenType::_type_ids_16bit();
            writeSize((int(ids.size()) << 2) | 0x01);
            for (std::size_t i = 0; i < ids.size(); i++)
                write(ids[i], false);
            writeSize(nFieldsToVisit);
        }

    }

    void endVisit() {
    }

private:

    void writePoly(const MGenBase& v, const bool doTag) {
        writeTagIf(BINARY_TAG_CUSTOM, doTag);
        m_classRegistry.visitObject(v, *this, mgen::ALL_SET_NONTRANSIENT);
    }

    template<typename MGenType>
    void write(const MGenType& v, const MGenBase&, const bool doTag) {
        m_expectType = MGenType::_type_id;
        writeTagIf(BINARY_TAG_CUSTOM, doTag);
        v._accept(*this, ALL_SET_NONTRANSIENT);
    }

    template<typename EnumType>
    void write(const EnumType v, const int, const bool doTag) {
        write(get_enum_name(v), doTag);
    }

    template<typename MGenTypeOrEnum>
    void write(const MGenTypeOrEnum& v, const bool doTag) {
        write(v, v, doTag);
    }

    template<typename MGenType>
    void write(const Polymorphic<MGenType>& v, const bool doTag) {
        if (v.get()) {
            m_expectType = MGenType::_type_id;
            writePoly(*v, doTag);
        } else {
            writeTagIf(BINARY_TAG_CUSTOM, doTag);
            writeSize(0);
        }
    }

    template<typename T>
    void write(const std::vector<T>& v, const bool doTag) {
        writeTagIf(BINARY_TAG_LIST, doTag);
        writeSize(v.size());
        if (!v.empty()) {
            writeTagIf(BINARY_TAG_OF((T*) 0), true);
            for (std::size_t i = 0; i < v.size(); i++)
                write(v[i], false);
        }
    }

    template<typename K, typename V>
    void write(const std::map<K, V>& v, const bool doTag) {
        writeTagIf(BINARY_TAG_MAP, doTag);
        writeSize(v.size());
        if (!v.empty()) {

            writeTagIf(BINARY_TAG_OF((K*) 0), true);
            writeTagIf(BINARY_TAG_OF((V*) 0), true);

            for (typename std::map<K, V>::const_iterator it = v.begin(); it != v.end(); it++) {
                write(it->first, false);
                write(it->second, false);
            }

        }
    }

    void write(const bool v, const bool doTag) {
        writeTagIf(BINARY_TAG_BOOL, doTag);
        writeByte(v ? 0x01 : 0x00);
    }

    void write(const char v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT8, doTag);
        writeByte(v);
    }

    void write(const short v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT16, doTag);
        write16(reinterpret_cast<const unsigned short&>(v));
    }

    void write(const int v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT32, doTag);
        writeSignedVarint32(v);
    }

    void write(const long long v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT64, doTag);
        writeSignedVarint64(v);
    }

    void write(const float v, const bool doTag) {
        writeTagIf(BINARY_TAG_FLOAT32, doTag);
        write32(reinterpret_cast<const unsigned int&>(v));
    }

    void write(const double v, const bool doTag) {
        writeTagIf(BINARY_TAG_FLOAT64, doTag);
        write64(reinterpret_cast<const unsigned long long&>(v));
    }

    void write(const std::string& v, const bool doTag) {
        writeTagIf(BINARY_TAG_STRING, doTag);
        writeSize(v.size());
        if (!v.empty())
            m_outputStream.write(v.data(), v.size());
    }

    void writeFieldStart(const short fieldId, const char tag) {
        write(fieldId, false);
        writeTypeTag(tag);
    }

    void writeTagIf(const BINARY_TAG tagValue, const bool doTag) {
        if (doTag)
            writeTypeTag(tagValue);
    }

    void writeTypeTag(const char tag) {
        write(tag, false);
    }

    void writeSize(const int sz) {
        writeUnsignedVarint32(sz);
    }

    void writeUnsignedVarint32(const unsigned int v) {
        varint::writeUnsigned32(v, m_outputStream);
    }

    void writeSignedVarint32(const int v) {
        varint::writeSigned32(v, m_outputStream);
    }

    void writeSignedVarint64(const long long v) {
        varint::writeSigned64(v, m_outputStream);
    }

    void writeByte(const char c) {
        m_outputStream.write(&c, 1);
    }

    void write16(const unsigned short data) {
        unsigned char buf[2];
        buf[0] = (unsigned char)(data >> 8);
        buf[1] = (unsigned char)(data >> 0);
        m_outputStream.write(buf, 2);
    }

    void write32(const unsigned int data) {
        unsigned char buf[4];
        buf[0] = (unsigned char)(data >> 24);
        buf[1] = (unsigned char)(data >> 16);
        buf[2] = (unsigned char)(data >> 8);
        buf[3] = (unsigned char)(data >> 0);
        m_outputStream.write(buf, 4);
    }

    void write64(const unsigned long long data) {
        unsigned char buf[8];
        buf[0] = (unsigned char)(data >> 56);
        buf[1] = (unsigned char)(data >> 48);
        buf[2] = (unsigned char)(data >> 40);
        buf[3] = (unsigned char)(data >> 32);
        buf[4] = (unsigned char)(data >> 24);
        buf[5] = (unsigned char)(data >> 16);
        buf[6] = (unsigned char)(data >> 8);
        buf[7] = (unsigned char)(data >> 0);
        m_outputStream.write(buf, 8);
    }

    bool shouldOmitIds(const long long expId) {
        return m_compact && expId == m_expectType;
    }

    const bool m_compact;
    long long m_expectType;
    MGenStreamType& m_outputStream;
    const ClassRegistryType& m_classRegistry;
};

} /* namespace mgen */

#endif /* MGEN_MGENBINARYWRITER_H_ */

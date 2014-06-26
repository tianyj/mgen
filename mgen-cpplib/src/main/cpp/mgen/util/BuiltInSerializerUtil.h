/*
 * BuiltInSerializerUtil.h
 *
 *  Created on: 24 jun 2014
 *      Author: GiGurra
 */

#ifndef BUILTINSERIALIZERUTIL_H_
#define BUILTINSERIALIZERUTIL_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/util/stringutil.h"
#include "mgen/exceptions/UnexpectedTypeException.h"

namespace mgen {
namespace serialutil {

template<typename ReaderType, typename ClassRegType, typename ContextType>
MGenBase * readObjInternal(
        ReaderType& reader,
        const ClassRegType& classRegistry,
        ContextType& context,
        MGenBase * object,
        const ClassRegistryEntry& entry) {

    if (!object) {
        try {
            object = entry.newInstance();
            classRegistry.readObjectFields(*object, context, reader);
        } catch (...) {
            delete object;
            throw;
        }
    } else {
        classRegistry.readObjectFields(*object, context, reader);
    }

    return object;

}

template<typename ClassRegType, typename IdType>
const ClassRegistryEntry * getCompatibleEntry(
        const ClassRegType& classReg,
        const std::vector<IdType>& ids,
        const bool constrained,
        const long constraint) {

    const ClassRegistryEntry * entry = classReg.getByIds(ids);

    if (constrained) {
        if (!entry) {
            new UnexpectedTypeException("Unknown type: " + toString(ids));
        } else if (!entry->isInstanceOfTypeId(constraint)) {
            const ClassRegistryEntry * expEntry = classReg.getByTypeId(constraint);
            new UnexpectedTypeException(
                    std::string("Unexpected type. Expected ")
                    .append(expEntry ? expEntry->typeName() : " <unknown> ")
                    .append(" but got ")
                    .append(entry->typeName()));
        }
    }

    return entry;

}

}
/* namespace serialutil */
} /* namespace mgen */

#endif /* BUILTINSERIALIZERUTIL_H_ */
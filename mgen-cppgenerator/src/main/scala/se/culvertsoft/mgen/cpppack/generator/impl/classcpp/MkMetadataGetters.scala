package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._

object MkMetadataGetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    ln(s"const long long ${t.shortName()}::_typeId() const {")
    ln(1, s"return _type_id;")
    ln(s"}")
    endl()

    ln(s"const std::string& ${t.shortName()}::_typeName() const {")
    ln(1, s"return _type_name();")
    ln(s"}")
    endl()

    ln(s"const short ${t.shortName()}::_typeId16Bit() const {")
    ln(1, s"return _type_id_16bit;")
    ln(s"}")
    endl()

    ln(s"const std::vector<long long>& ${t.shortName()}::_typeIds() const {")
    ln(1, s"return _type_ids();")
    ln(s"}")
    endl()
    
    ln(s"const std::vector<short>& ${t.shortName()}::_typeIds16Bit() const {")
    ln(1, s"return _type_ids_16bit();")
    ln(s"}")
    endl()

    ln(s"const std::string& ${t.shortName()}::_typeId16BitBase64() const {")
    ln(1, s"return _type_id_16bit_base64();")
    ln(s"}")
    endl()

    
    ln(s"const std::vector<std::string>& ${t.shortName()}::_typeNames() const {")
    ln(1, s"return _type_names();")
    ln(s"}")
    endl()

    ln(s"const std::vector<std::string>& ${t.shortName()}::_typeIds16BitBase64() const {")
    ln(1, s"return _type_ids_16bit_base64();")
    ln(s"}")
    endl()

    ln(s"const std::string& ${t.shortName()}::_typeIds16BitBase64String() const {")
    ln(1, s"return _type_ids_16bit_base64_string();")
    ln(s"}")
    endl()
    
    ln(s"const std::vector<mgen::Field>& ${t.shortName()}::_fieldMetadatas() const {")
    ln(1, s"return _field_metadatas();")
    ln(s"}")
    endl()


  }

}
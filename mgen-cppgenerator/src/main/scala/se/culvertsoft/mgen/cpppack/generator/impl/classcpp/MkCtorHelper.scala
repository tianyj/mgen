package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenerator._

object MkCtorHelper {

  def mkPassToSuper(
    fieldsToSuper: Seq[Field],
    t: CustomType,
    module: Module): String = {

    var passToSuperString = ""

    if (fieldsToSuper.nonEmpty) {
      passToSuperString += s"${CppGenUtils.getSuperTypeString(t)}("
      for (i <- 0 until fieldsToSuper.size) {
        val field = fieldsToSuper(i)
        val isLastField = i + 1 == fieldsToSuper.size
        passToSuperString += field.name
        if (!isLastField) {
          passToSuperString += ", "
        }
      }
      passToSuperString += ")"
    }

    passToSuperString
  }

  def mkReqNonNullFields(fields: Seq[Field]): Seq[String] = {
    fields map { field =>
      s"${isSetName(field)}(${if (field.isRequired) "true" else "false"})"
    }
  }

  def mkReqMemberValues(fields: Seq[Field], module: Module): Seq[String] = {
    implicit val currentModule = module
    fields.filterNot(canBeNull) map { field =>
      if (field.isRequired())
        s"m_${field.name()}(${field.name()})"
      else
        s"m_${field.name()}(${CppConstruction.defaultConstructNull(field)})"
    }
  }

}
package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkDeepCopy {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(1).textln(s"${t.shortName()} * _deepCopy() const;").endl()

  }

}
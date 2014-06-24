package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.mapAsScalaMap

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkLongTypeName

object CppHandlerSrcFileGenerator extends CppHandlerGenerator(SrcFile) {

  override def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include("Handler.h")
    endl()
  }

  override def mkClassContents(param: UtilClassGenParam) {
    super.mkClassContents(param)

    val allTypes = param.modules.flatMap(_.types).map(_._2).distinct
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    ln("Handler::Handler() {}").endl()
    ln("Handler::~Handler() {}").endl()

    def mkDefaultHandlers() {
      ln("void Handler::handleDiscard(mgen::MGenBase& o) {")
      ln("}").endl()
      ln("void Handler::handleUnknown(mgen::MGenBase& o) {")
      ln(1, s"handleDiscard(o);")
      ln("}").endl()
    }

    def mkHandler(t: CustomType) {
      val passCall = (if (t.hasSuperType()) s"handle(reinterpret_cast<${MkLongTypeName.cpp(t.superType.asInstanceOf[CustomType])}&>(o))" else "handleDiscard(o)")
      ln(s"void Handler::handle(${MkLongTypeName.cpp(t)}& o) {")
      ln(1, s"$passCall;")
      ln("}")
      endl()
    }

    def mkHandlers() {
      allTypes foreach mkHandler
    }

    mkDefaultHandlers()
    mkHandlers()

    endl()

  }

}
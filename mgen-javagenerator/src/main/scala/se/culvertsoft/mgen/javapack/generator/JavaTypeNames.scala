package se.culvertsoft.mgen.javapack.generator

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.MGenBaseType
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.Type
import scala.collection.mutable.HashMap
import se.culvertsoft.mgen.api.model.Module

object JavaTypeNames {

	val typeStringCache = new HashMap[(Type, Boolean, Boolean), String]

	def getTypeName(
		typ: Type,
		isGenericArg: Boolean = false,
		isArrayCtor: Boolean = false)(implicit currentModule: Module): String = {
		typeStringCache.getOrElseUpdate((typ, isGenericArg, isArrayCtor), {
			typ.typeEnum() match {
				case TypeEnum.BOOL => if (isGenericArg) "Boolean" else "boolean"
				case TypeEnum.INT8 => if (isGenericArg) "Byte" else "byte"
				case TypeEnum.INT16 => if (isGenericArg) "Short" else "short"
				case TypeEnum.INT32 => if (isGenericArg) "Integer" else "int"
				case TypeEnum.INT64 => if (isGenericArg) "Long" else "long"
				case TypeEnum.FLOAT32 => if (isGenericArg) "Float" else "float"
				case TypeEnum.FLOAT64 => if (isGenericArg) "Double" else "double"
				case TypeEnum.STRING => "String"
				case TypeEnum.MAP =>
					val t = typ.asInstanceOf[MapType]
					s"java.util.HashMap<${getTypeName(t.keyType(), true)}, ${getTypeName(t.valueType(), true)}>"
				case TypeEnum.LIST =>
					val t = typ.asInstanceOf[ListType]
					s"java.util.ArrayList<${getTypeName(t.elementType(), true)}>"
				case TypeEnum.ARRAY =>
					val t = typ.asInstanceOf[ArrayType]
					if (isArrayCtor)
						s"${getTypeName(t.elementType(), false)}[0]"
					else
						s"${getTypeName(t.elementType(), false)}[]"
				case TypeEnum.CUSTOM =>
					val t = typ.asInstanceOf[CustomType]
					if (t.module() == currentModule) {
						t.name()
					} else {
						t.fullName()
					}
				case TypeEnum.MGEN_BASE => MGenBaseType.INSTANCE.fullName()
				case x => throw new GenerationException(s"Don't know how to handle type $x (${typ.fullName})")
			}
		})
	}

}
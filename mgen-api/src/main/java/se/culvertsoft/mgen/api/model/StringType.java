package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.StringTypeImpl;

public interface StringType extends SimpleType {

	public final static StringType INSTANCE = StringTypeImpl.INSTANCE;

}

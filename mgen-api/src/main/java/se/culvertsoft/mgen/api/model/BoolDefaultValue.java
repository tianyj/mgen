package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a bool field/type.
 */
public class BoolDefaultValue extends DefaultValue {

	/**
	 * Returns the boolean value represented by this default value
	 */
	public boolean value() {
		return m_value;
	}

	/**
	 * The type of this bool default value
	 */
	@Override
	public BoolType expectedType() {
		return (BoolType) super.expectedType();
	}

	public BoolDefaultValue(final boolean value, final ClassType referencedFrom) {
		super(BoolType.INSTANCE, referencedFrom);
		m_value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(m_value);
	}

	private final boolean m_value;

}

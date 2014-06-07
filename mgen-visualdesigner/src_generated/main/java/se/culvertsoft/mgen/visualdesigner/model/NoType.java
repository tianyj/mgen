/********************************************************************************************************************
 ********************************************************************************************************************
 **********************************************************************************************************	
           *****                                                                                      *****
           *****                               GENERATED WITH MGEN                                    *****
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/
package se.culvertsoft.mgen.visualdesigner.model;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;
import se.culvertsoft.mgen.javapack.serialization.FieldVisitor;
import se.culvertsoft.mgen.javapack.serialization.Reader;

public class NoType extends BuiltInType {

    public NoType() {
        super();
    }

    @Override
    public String toString() {
        return _typeName() + "_instance";
    }

    @Override
    public int hashCode() {
        return -717943251;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (NoType.class != other.getClass()) return false;
        return true;
    }

    @Override
    public NoType deepCopy() {
        final NoType out = new NoType();
        return out;
    }


							
/********************************************************************************************************************
 ********************************************************************************************************************
 **********************************************************************************************************	
           *****                                                                                      *****
           *****                    TYPE METADATA ACCESS AND SERIALIZATION METHODS                    *****
           *****          (accessed primarily by (de-)serializers and for ORM functionality)          *****	
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/	 		  
		  

    @Override
    public String _typeName() {
        return _TYPE_NAME;
    }

    @Override
    public short _typeHash16bit() {
        return _TYPE_HASH_16BIT;
    }

    @Override
    public int _typeHash32bit() {
        return _TYPE_HASH_32BIT;
    }

    @Override
    public void _accept(final FieldVisitor visitor) throws java.io.IOException {
        visitor.beginVisit(this, _nFieldsSet(FieldSetDepth.SHALLOW));
        visitor.endVisit();
    }

    @Override
    public boolean _readField(final Field field,
                             final Object context,
                             final Reader reader) throws java.io.IOException {
        switch(field.fieldHash16bit()) {
            default:
                reader.handleUnknownField(field, context);
                return false;
        }
    }

    @Override
    public java.util.Collection<Field> _fields() {
        return FIELDS;
    }

    public boolean _isFieldSet(final Field field, final FieldSetDepth depth) {
        switch(field.fieldHash16bit()) {
            default:
                return false;
        }
    }

    public NoType _setAllFieldsSet(final boolean state, final FieldSetDepth depth) { 
        return this;
    }

    public boolean _validate(final FieldSetDepth fieldSetDepth) { 
        if (fieldSetDepth == FieldSetDepth.SHALLOW) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public int _nFieldsSet(final FieldSetDepth fieldSetDepth) {
        int out = 0;
        return out;
    }

    @Override
    public Field _fieldBy16BitHash(final short hash) {
        switch(hash) {
            default:
                return null;
        }
    }

    @Override
    public Field _fieldBy32BitHash(final int hash) {
        switch(hash) {
            default:
                return null;
        }
    }

    @Override
    public short[] _typeHashes16bit() {
        return _TYPE_HASHES_16BIT;
    }

    @Override
    public int[] _typeHashes32bit() {
        return _TYPE_HASHES_32BIT;
    }

    @Override
    public java.util.Collection<String> _typeNames() {
        return _TYPE_NAMES;
    }

    @Override
    public java.util.Collection<String> _typeHashes16bitBase64() {
        return _TYPE_HASHES_16BIT_BASE64;
    }

    @Override
    public java.util.Collection<String> _typeHashes32bitBase64() {
        return _TYPE_HASHES_32BIT_BASE64;
    }


							
/********************************************************************************************************************
 ********************************************************************************************************************
 **********************************************************************************************************	
           *****                                                                                      *****
           *****                                    TYPE METADATA                                     *****
           *****             (generally speaking, it's a bad idea to edit this manually)              *****	
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/	 		  
		  

    public static final String _TYPE_NAME = "se.culvertsoft.mgen.visualdesigner.model.NoType";
    public static final short _TYPE_HASH_16BIT = 15271;
    public static final int _TYPE_HASH_32BIT = -252821480;

    public static final java.util.Collection<Field> FIELDS;

    public static final short[] _TYPE_HASHES_16BIT;
    public static final int[] _TYPE_HASHES_32BIT;
    public static final java.util.Collection<String> _TYPE_NAMES;
    public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;
    public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;

    static {
        final java.util.ArrayList<Field> fields = new java.util.ArrayList<Field>();
        FIELDS = fields;
    }

    static {
        _TYPE_HASHES_16BIT = new short[3];
        _TYPE_HASHES_32BIT = new int[3];
        final java.util.ArrayList<String> names = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();
        final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();
        _TYPE_HASHES_16BIT[0] = 1314;
        _TYPE_HASHES_32BIT[0] = -298374023;
        names.add("se.culvertsoft.mgen.visualdesigner.model.FieldType");
        base6416bit.add("BSI");
        base6432bit.add("7jcseQ");
        _TYPE_HASHES_16BIT[1] = -27713;
        _TYPE_HASHES_32BIT[1] = -1000408287;
        names.add("se.culvertsoft.mgen.visualdesigner.model.BuiltInType");
        base6416bit.add("k78");
        base6432bit.add("xF77IQ");
        _TYPE_HASHES_16BIT[2] = 15271;
        _TYPE_HASHES_32BIT[2] = -252821480;
        names.add("se.culvertsoft.mgen.visualdesigner.model.NoType");
        base6416bit.add("O6c");
        base6432bit.add("8O5AGA");
        _TYPE_NAMES = names;
        _TYPE_HASHES_16BIT_BASE64 = base6416bit;
        _TYPE_HASHES_32BIT_BASE64 = base6432bit;
    }

}

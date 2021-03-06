package com.heliorm.query;

import com.heliorm.def.Field;

/**
 *
 * @author gideon
 */
public abstract class FieldCriteria extends Criteria {

    private final Field field;

    public FieldCriteria(Type type, Field field) {
        super(type);
        this.field = field;
    }

    public Field getField() {
        return field;
    }

}

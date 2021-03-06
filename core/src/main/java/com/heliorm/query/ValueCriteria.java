package com.heliorm.query;

import com.heliorm.def.Field;

/**
 *
 * @author gideon
 */
public class ValueCriteria extends FieldCriteria {

    public enum Operator {
        EQ, NOT_EQ, LT, LE, GT, GE, LIKE, NOT_LIKE;
    }

    private final Object value;
    private final Operator operator;

    public ValueCriteria(Field field, Operator operator, Object value) {
        super(Type.VALUE_FIELD, field);
        this.value = value;
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

}

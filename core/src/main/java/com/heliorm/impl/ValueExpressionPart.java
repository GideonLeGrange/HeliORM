package com.heliorm.impl;

import com.heliorm.Table;

import static java.lang.String.format;

/**
 * @param <T> Type of table
 * @param <O> Type of POJO
 * @param <C> Type of the field
 * @author gideon
 */
public class ValueExpressionPart<T extends Table<O>, O, C> extends ExpressionPart<T, O, C> {

    private final Operator operator;
    private final Object value;

    public enum Operator {
        EQ, NOT_EQ, LT, LE, GT, GE, LIKE, NOT_LIKE;
    }

    public ValueExpressionPart(FieldPart left, Operator op, C value) {
        super(Type.VALUE_EXPRESSION, left);
        this.operator = op;
        this.value = value;
    }


    public Operator getOperator() {
        return operator;
    }

    public C getValue() {
        return (C) value;
    }

    @Override
    public String toString() {
        return format("%s '%s'", operator.name(), value);
    }

}

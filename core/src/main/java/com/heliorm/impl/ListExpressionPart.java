package com.heliorm.impl;

import com.heliorm.Table;
import com.heliorm.def.Field;

import java.util.List;

import static java.lang.String.format;

/**
 *
 * @author gideon
 */
public abstract class ListExpressionPart<T extends Table<O>, O, C> extends ExpressionPart<T, O, C> {

    private final Field.FieldType dataType;
    private final Operator operator;

    public enum Operator {
        IN, NOT_IN;
    }

    protected ListExpressionPart(Field.FieldType dataType, FieldPart left, Operator op) {
        super(Type.LIST_EXPRESSION, left);
        this.operator = op;
        this.dataType = dataType;
    }

    public Operator getOperator() {
        return operator;
    }

    public  abstract List<C> getValues();

    @Override
    public String toString() {
        return format("%s (%s)", operator.name(), getValues());
    }
}

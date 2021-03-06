package com.heliorm.impl;

import com.heliorm.Table;
import com.heliorm.def.Field;

public final class ShortValueExpressionPart<T extends Table<O>, O> extends NumberValueExpressionPart<T,O, Short> {

    private Short value;

    public ShortValueExpressionPart(ShortFieldPart<T,O> left, Operator op, Short value) {
        super(Field.FieldType.SHORT, left, op);
        this.value = value;
    }

    @Override
    public Short getValue() {
        return value;
    }

}


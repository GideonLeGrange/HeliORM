package com.heliorm.impl;

import com.heliorm.def.ExpressionContinuation;
import com.heliorm.OrmException;
import com.heliorm.Table;

/**
 *
 * @author gideon
 * @param <T> Type of table
 * @param <O> Type of POJO
 */
public class ExpressionContinuationPart<T extends Table<O>, O> extends Part<T, O, T, O> implements ExpressionContinuation<T, O> {

    private final Part expression;

    public ExpressionContinuationPart(Part left, Type type, ExpressionContinuation expr) {
        super(type, left);
        expression = ((Part) expr).head();
    }

    @Override
    public ExpressionContinuation<T, O> and(ExpressionContinuation<T, O> expr) {
        return new ExpressionContinuationPart(this, Type.NESTED_AND, expr);
    }

    @Override
    public ExpressionContinuation<T, O> or(ExpressionContinuation<T, O> expr) {
        return new ExpressionContinuationPart(this, Type.NESTED_OR, expr);
    }

    public Part getExpression() throws OrmException {
        return expression;
    }

    @Override
    public String toString() {
        return getType().name();
    }
}

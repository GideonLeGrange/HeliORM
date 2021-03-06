package com.heliorm.def;

import com.heliorm.Table;

/**
 *
 * @author gideon
 * @param <T>
 * @param <O>
 */
public interface ExpressionContinuation<T extends Table<O>, O> {

    ExpressionContinuation<T, O> and(ExpressionContinuation<T, O> expr);

    ExpressionContinuation<T, O> or(ExpressionContinuation<T, O> expr);

}

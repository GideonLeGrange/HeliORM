package net.legrange.orm.def;

import net.legrange.orm.Table;

/**
 *
 * @author gideon
 * @param <T>
 * @param <O>
 * @param <RT>
 * @param <RO>
 */
public interface Select<T extends Table<O>, O, RT extends Table<RO>, RO> extends Executable<T, O>, Order<T, O> {

    <RT extends Table<RO>, RO> Join<T, O, RT, RO> join(RT table);

    Continuation<T, O, RT, RO> where(ExpressionContinuation<RT, RO> cont);

}
package com.heliorm.def;

import com.heliorm.Table;

/**
 *
 * @author gideon
 * @param <T> Type of the table
 * @param <O> Type of the POJO
 */
public interface Ordered<T extends Table<O>, O> extends Executable<T, O> {

    <F extends Field<T, O, C>, C> Ordered<T, O> thenBy(F field);

    <F extends Field<T, O, C>, C> Ordered<T, O> thenByDesc(F field);

}

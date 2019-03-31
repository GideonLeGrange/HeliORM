package me.legrange.orm.impl;

import me.legrange.orm.Orm;
import me.legrange.orm.Table;

/**
 *
 * @author gideon
 */
public abstract class Part<T extends Table<O>, O, RT extends Table<RO>, RO> {

    public enum Type {
        SELECT, VALUE_OPERATION, LIST_OPERATION, ON_CLAUSE, CLAUSE, JOIN,
        ON_EQ,
        CONTINUATION, ORDER;
    }

    private final Part left;

    protected Part(Part left) {
        this.left = left;
    }

    protected Orm getOrm() {
        return left.getOrm();
    }

    public abstract Type getType();

    public Table getReturnTable() {
        return left.getReturnTable();
    }

    public Table getSelectTable() {
        return left.getSelectTable();
    }

    public Part getLeft() {
        return left;
    }

}

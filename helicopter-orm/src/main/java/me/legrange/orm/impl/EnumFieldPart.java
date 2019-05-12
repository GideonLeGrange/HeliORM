package me.legrange.orm.impl;

import me.legrange.orm.EnumField;
import me.legrange.orm.Table;

/**
 *
 * @author gideon
 * @param <T>
 * @param <O>
 * @param <E>
 */
public class EnumFieldPart<T extends Table<O>, O, E extends Enum> extends FieldPart<T, O, E> implements
        EnumField<T, O, E>,
        WithEqualsPart<T, O, E>,
        WithInPart<T, O, E> {

    public EnumFieldPart(Class<E> fieldType, String javaName, String sqlName) {
        super(fieldType, javaName, sqlName);
    }

}

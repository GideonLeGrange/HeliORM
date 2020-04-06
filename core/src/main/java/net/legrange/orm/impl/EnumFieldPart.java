package net.legrange.orm.impl;

import net.legrange.orm.Table;
import net.legrange.orm.def.EnumField;

/**
 * @param <T>
 * @param <O>
 * @param <E>
 * @author gideon
 */
public class EnumFieldPart<T extends Table<O>, O, E extends Enum> extends FieldPart<T, O, E> implements
        EnumField<T, O, E>,
        WithEqualsPart<T, O, E>,
        WithInPart<T, O, E> {

    public EnumFieldPart(Class<E> fieldType, String javaName, String sqlName) {
        super(fieldType, javaName, sqlName);
    }

}
package net.legrange.orm.def;

import net.legrange.orm.Table;

import java.util.Collections;
import java.util.Set;

/**
 * A field on a table.
 *
 * @param <T> The type of the table
 * @param <O> The type of the POJO to which the table and this field applies
 * @param <C> The data type of the field
 * @author gideon
 */
public interface Field<T extends Table<O>, O, C> {

    public enum FieldType {
        LONG, INTEGER, SHORT, BYTE,
        DOUBLE, FLOAT,
        BOOLEAN,
        ENUM,
        STRING,
        DATE, TIMESTAMP, DURATION;
    }

    /**
     * Get the type of the field.
     *
     * @return The field type
     */
    FieldType getFieldType();

    /**
     * Get the java type fo the field
     *
     * @return The java type
     */
    Class<C> getJavaType();

    /**
     * Get the java field name of the field.
     *
     * @return The field name
     */
    String getJavaName();

    /**
     * Get the SQL column name of the field.
     *
     * @return The SQL column name
     */
    String getSqlName();

    /**
     * Returns true if this field represents the table's primary key.
     *
     * @return True if it does
     */
    boolean isPrimaryKey();

    /**
     * Return true if this is an auto-number key
     *
     * @return True if auto-number
     */
    boolean isAutoNumber();

    default Set<String> getEnumValues() {
        return Collections.EMPTY_SET;
    }

}

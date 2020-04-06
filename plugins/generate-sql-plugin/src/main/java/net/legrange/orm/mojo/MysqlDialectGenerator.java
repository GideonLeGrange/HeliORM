package net.legrange.orm.mojo;

import net.legrange.orm.Table;
import net.legrange.orm.def.Field;

import java.util.Optional;
import java.util.StringJoiner;

import static java.lang.String.format;

public class MysqlDialectGenerator implements DialectGenerator {
    @Override
    public String generateSql(Table<?> table) throws GeneratorException {
        StringBuilder sql = new StringBuilder();
        sql.append(format("CREATE TABLE `%s` (\n", table.getSqlTable()));
        boolean first = true;
        for (Field field : table.getFields()) {
            if (first) {
                first = false;
            } else {
                sql.append(",\n");
            }
            sql.append(format("\t`%s` ", field.getSqlName()));
            sql.append(generateFieldSql(table, field));
            sql.append(" NOT NULL");
            if (field.isPrimaryKey() && field.isAutoNumber() && field.getFieldType() != Field.FieldType.STRING) {
                sql.append(" AUTO_INCREMENT");
            }
        }
        Optional<Field> key = table.getPrimaryKey();
        if (key.isPresent()) {
            sql.append(",\n");
            sql.append(format("PRIMARY KEY (`%s`)", key.get().getSqlName()));
        }
        sql.append(");\n");
        return sql.toString();
    }

    private String generateFieldSql(Table table, Field field) throws GeneratorException {
        switch (field.getFieldType()) {
            case BOOLEAN:
                return "TINYINT(1)";
            case BYTE:
                return "TINYINT";
            case SHORT:
                return "SMALLINT";
            case INTEGER:
                return "INTEGER";
            case LONG:
                return "BIGINT";
            case DOUBLE:
                return "DOUBLE";
            case FLOAT:
                return "REAL";
            case ENUM:
                return format("ENUM(%s)", getEnumValues(table, field));
            case STRING: {
                int length = 255;
                if (field.getLength().isPresent()) {
                    length = (int) field.getLength().get();
                }
                return format("VARCHAR(%d)", length);
            }
            case DATE:
                return "DATE";
            case TIMESTAMP:
                return "DATETIME";
            case DURATION:
                return "VARCHAR(32)";
            default:
                throw new GeneratorException(format("Unkown field type '%s'. BUG!", field.getFieldType()));
        }
    }

    private String getEnumValues(Table table, Field<?, ?, ?> field) {
        StringJoiner sql = new StringJoiner(",");
        Class<?> javaType = field.getJavaType();
        for (Object v : javaType.getEnumConstants()) {
            sql.add(format("'%s'", ((Enum) v).name()));
        }
        return sql.toString();
    }
}
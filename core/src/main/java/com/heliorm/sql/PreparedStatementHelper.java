package com.heliorm.sql;

import com.heliorm.OrmException;
import com.heliorm.def.Field;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.String.format;

class PreparedStatementHelper {

    private final PojoHelper pojoHelper;
    private final SetEnum setEnum;

    public PreparedStatementHelper(PojoHelper pojoHelper, SetEnum setEnum) {
        this.pojoHelper = pojoHelper;
        this.setEnum = setEnum;
    }

    /**
     * Set the value in a prepared statement to the value of the given field
     * from the given POJO
     *
     * @param stmt  The prepared statement in which to set the value
     * @param pojo  The POJO from which to obtain the value
     * @param field The field for which to get the value from the POJO
     * @param par   The position in the prepared statement for the value
     * @throws OrmException
     */
     void setValueInStatement(PreparedStatement stmt, Object pojo, Field field, int par) throws OrmException {
        try {
            switch (field.getFieldType()) {
                case LONG:
                case INTEGER:
                case SHORT:
                case BYTE:
                case DOUBLE:
                case FLOAT:
                case BOOLEAN:
                    stmt.setObject(par, pojoHelper.getValueFromPojo(pojo, field));
                    break;
                case ENUM:
                    setEnum.apply(stmt, par, pojoHelper.getStringFromPojo(pojo, field));
                    break;
                case STRING:
                    stmt.setString(par, pojoHelper.getStringFromPojo(pojo, field));
                    break;
                case DATE:
                    stmt.setDate(par, pojoHelper.getDateFromPojo(pojo, field));
                    break;
                case INSTANT:
                    stmt.setTimestamp(par, pojoHelper.getTimestampFromPojo(pojo, field));
                    break;
                case DURATION:
                    stmt.setString(par, pojoHelper.getDurationFromPojo(pojo, field));
                    break;
                default:
                    throw new OrmException(format("Field type '%s' is unsupported. BUG!", field.getFieldType()));
            }
        } catch (SQLException ex) {
            throw new OrmSqlException(ex.getMessage(), ex);
        }
    }

}

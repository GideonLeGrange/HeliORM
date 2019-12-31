package net.legrange.orm.driver;

import net.legrange.orm.OrmException;
import net.legrange.orm.OrmTransaction;
import net.legrange.orm.OrmTransactionException;

import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.String.format;

class SqlTransaction implements OrmTransaction, AutoCloseable {

    private final SqlDriver driver;
    private final Connection connection;
    private boolean open;

    SqlTransaction(SqlDriver driver) throws OrmTransactionException {
        this.driver = driver;
        this.connection = driver.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new OrmTransactionException(format("Cannot disable SQL connection auto-commit (%s)", e.getMessage()));
        }
        this.open = true;
    }

    @Override
    public void commit() throws OrmException {
        try {
            if (!open) {
                throw new OrmTransactionException(format("Cannot commit an already comitted or rolled back SQL transaction"));
            }
            connection.commit();
            open = false;
        }
        catch (SQLException ex) {
            throw new OrmException(format("Error commiting transaction (%s)", ex.getMessage()));
        }
    }

    @Override
    public void rollback() throws OrmException {
        try {
            if (!open) {
                throw new OrmTransactionException(format("Cannot commit an already comitted or rolled back SQL transaction"));
            }
            connection.rollback();
            open = false;
        }
        catch (SQLException ex) {
            throw new OrmException(format("Error rolling back transaction (%s)", ex.getMessage()));
        }

    }

    @Override
    public void close() throws OrmException {
        if (open) {
            if (!driver.getRollbackOnUncommittedClose()) {
                open = false;
                throw new OrmException("Transaction is being auto-closed without committing or rolling back");
            }
            rollback();
        }
    }

    Connection getConnection() {
        return connection;
    }

    boolean isOpen() {
        return open;
    }

}
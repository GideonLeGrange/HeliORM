package net.legrange.orm.mojo;

import net.legrange.orm.OrmException;

/**
 * Exception thrown when the meta data generator encounters and error. error.
 *
 * @author gideon
 */
public class OrmMetaDataException extends OrmException {

    public OrmMetaDataException(String message) {
        super(message);
    }

    public OrmMetaDataException(String message, Throwable cause) {
        super(message, cause);
    }

}

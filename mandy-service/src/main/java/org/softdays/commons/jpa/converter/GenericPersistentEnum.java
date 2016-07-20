package org.softdays.commons.jpa.converter;

/**
 * The Interface GenericPersistentEnum.
 */
public interface GenericPersistentEnum<P> {

    /**
     * Gets the database id (will be used as primary key).
     *
     * @return the id
     */
    P getPk();

}

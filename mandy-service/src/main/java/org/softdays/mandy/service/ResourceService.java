package org.softdays.mandy.service;

import org.softdays.mandy.dto.ResourceDto;

/**
 * Resource service.
 * 
 * @author rpatriarche
 * 
 */
public interface ResourceService {

    /**
     * Returns a resource from an LDAP UID.
     * 
     * @param userLogin
     *            The user LDAP login.
     * 
     * @return A instance of {@link ResourceDto}.
     */
    ResourceDto findByUid(String userLogin);

    /**
     * Returns
     * 
     * @param uid
     *            Identifiant LDAP.
     * 
     * @param lastname
     *            The last name of the user to create.
     * @param firstname
     *            The first name of the user to create.
     * 
     * @return A instance of {@link ResourceDto}.
     */
    ResourceDto create(String uid, String lastname, String firstname);

}

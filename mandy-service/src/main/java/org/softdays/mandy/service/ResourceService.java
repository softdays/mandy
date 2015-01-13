/*
 * MANDY is a simple webapp to track man-day consumption on activities.
 * 
 * Copyright 2014, rpatriarche
 *
 * This file is part of MANDY software.
 *
 * MANDY is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * MANDY is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.softdays.mandy.service;

import org.softdays.mandy.dto.PreferencesDto;
import org.softdays.mandy.dto.ResourceDto;

/**
 * The Interface ResourceService.
 * 
 * @author rpatriarche
 * @since 1.0.0
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
     * Returns a resource DTO created from arguments.
     * 
     * @param uid
     *            Identifiant LDAP.
     * @param lastname
     *            The last name of the user to create.
     * @param firstname
     *            The first name of the user to create.
     * @return A instance of {@link ResourceDto}.
     */
    ResourceDto create(String uid, String lastname, String firstname);

    /**
     * Find resource global preferences.
     * 
     * @since 1.1.0
     * 
     * @param resourceId
     *            the resource id
     * @return the preferences dto
     */
    PreferencesDto findResourcePreferences(Long resourceId);

    /**
     * Updates resources preferences.
     * 
     * @param preferences
     *            The DTO to use to retrieve new preferences values.
     * @return The same DTO.
     */
    PreferencesDto updateResourcePreferences(PreferencesDto preferences);

}

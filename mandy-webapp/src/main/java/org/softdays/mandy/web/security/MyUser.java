/**
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
package org.softdays.mandy.web.security;

import java.util.Collection;

import org.softdays.mandy.dto.ResourceDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * The Class MyUser.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class MyUser extends User {

    private static final long serialVersionUID = 1L;

    private ResourceDto resource;

    /**
     * Instantiates a new my user.
     * 
     * @param resource
     *            the resource
     * @param authorities
     *            the authorities
     */
    public MyUser(ResourceDto resource,
	    Collection<? extends GrantedAuthority> authorities) {
	super(resource.getLogin(), "N/A", authorities);
	this.resource = resource;
    }

    /**
     * Gets the resource.
     * 
     * @return the resource
     */
    public ResourceDto getResource() {
	return resource;
    }

}

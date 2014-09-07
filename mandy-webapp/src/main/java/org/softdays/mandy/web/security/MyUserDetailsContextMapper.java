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

import java.util.Arrays;
import java.util.Collection;

import org.softdays.mandy.config.Configuration;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

/**
 * Retrieves user details from database.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Component
public class MyUserDetailsContextMapper implements UserDetailsContextMapper {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private Configuration configurationDto;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.ldap.userdetails.UserDetailsContextMapper
     * #mapUserFromContext(org.springframework.ldap.core.DirContextOperations,
     * java.lang.String, java.util.Collection)
     */
    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx,
	    String username, Collection<? extends GrantedAuthority> authorities) {
	ResourceDto resource = resourceService.findByUid(username);
	if (resource == null) {
	    String lastname = (String) ctx.getObjectAttribute(configurationDto
		    .getLdapAttributeForLastname());
	    String firstname = (String) ctx.getObjectAttribute(configurationDto
		    .getLdapAttributeForFirstname());
	    resource = resourceService.create(username, lastname, firstname);
	}
	String role = resource.getRole();
	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

	return new MyUser(resource, Arrays.asList(authority));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.ldap.userdetails.UserDetailsContextMapper
     * #mapUserToContext
     * (org.springframework.security.core.userdetails.UserDetails,
     * org.springframework.ldap.core.DirContextAdapter)
     */
    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
	// not used
    }

}

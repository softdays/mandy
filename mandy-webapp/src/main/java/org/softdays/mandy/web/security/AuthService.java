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
package org.softdays.mandy.web.security;

import org.softdays.mandy.dto.ResourceDto;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * The Class AuthService.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
public class AuthService {

    /**
     * Instantiates a new auth service.
     */
    public AuthService() {
        super();
    }

    /**
     * Gets the current token.
     * 
     * @return the current token
     */
    public ResourceDto getCurrentToken() {
        final SecurityContext ctx = SecurityContextHolder.getContext();
        final MyUser user = (MyUser) ctx.getAuthentication().getPrincipal();

        return user == null ? null : user.getResource();
    }

    /**
     * Returns the current user resource id.
     * 
     * @since 1.0.1
     * 
     * @return the resource ID.
     */
    public Long getCurrentUserId() {
        return this.getCurrentToken().getResourceId();
    }

}

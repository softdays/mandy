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
package org.softdays.mandy.dto;

/**
 * The Class ResourceDto.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class ResourceDto {

    private Long resourceId;

    private String login;

    private String lastName;

    private String firstName;

    private String role;

    /**
     * Gets the resource id.
     * 
     * @return the resource id
     */
    public Long getResourceId() {
	return resourceId;
    }

    /**
     * Sets the resource id.
     * 
     * @param resourceId
     *            the new resource id
     */
    public void setResourceId(Long resourceId) {
	this.resourceId = resourceId;
    }

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public String getLogin() {
	return login;
    }

    /**
     * Sets the login.
     * 
     * @param login
     *            the new login
     */
    public void setLogin(String login) {
	this.login = login;
    }

    /**
     * Gets the last name.
     * 
     * @return the last name
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * Sets the last name.
     * 
     * @param lastName
     *            the new last name
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * Gets the first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * Sets the first name.
     * 
     * @param firstName
     *            the new first name
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * Gets the role.
     * 
     * @return the role
     */
    public String getRole() {
	return role;
    }

    /**
     * Sets the role.
     * 
     * @param role
     *            the new role
     */
    public void setRole(String role) {
	this.role = role;
    }

}

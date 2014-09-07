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
package org.softdays.mandy.config;

public class Configuration {

    private String contextRoot = "mandy";

    private String ldapAttributeForLastname = "sn";

    private String ldapAttributeForFirstname = "givenName";

    private String version;

    public String getContextRoot() {
	return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
	this.contextRoot = contextRoot;
    }

    public String getLdapAttributeForLastname() {
	return ldapAttributeForLastname;
    }

    public void setLdapAttributeForLastname(String ldapAttributeForLastname) {
	this.ldapAttributeForLastname = ldapAttributeForLastname;
    }

    public String getLdapAttributeForFirstname() {
	return ldapAttributeForFirstname;
    }

    public void setLdapAttributeForFirstname(String ldapAttributeForFirstname) {
	this.ldapAttributeForFirstname = ldapAttributeForFirstname;
    }

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

}

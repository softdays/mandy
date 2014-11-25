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
package org.softdays.mandy.config;

/**
 * The Class Configuration.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class Configuration {

    private String contextRoot = "mandy";

    private String ldapAttrLastname = "sn";

    private String ldapAttrFirstname = "givenName";

    private String version;

    /**
     * Instantiates a new configuration.
     */
    public Configuration() {
        super();
    }

    /**
     * Gets the context root.
     * 
     * @return the context root
     */
    public String getContextRoot() {
        return this.contextRoot;
    }

    /**
     * Sets the context root.
     * 
     * @param contextRoot
     *            the new context root
     */
    public void setContextRoot(final String contextRoot) {
        this.contextRoot = contextRoot;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the version.
     * 
     * @param version
     *            the new version
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Gets the ldap attribute for lastname.
     * 
     * @return the ldap attribute for lastname
     */
    public String getLdapAttrLastname() {
        return this.ldapAttrLastname;
    }

    /**
     * Sets the ldap attribute for lastname.
     * 
     * @param ldapAttrLastname
     *            the new ldap attribute for lastname
     */
    public void setLdapAttrLastname(final String ldapAttrLastname) {
        this.ldapAttrLastname = ldapAttrLastname;
    }

    /**
     * Gets the ldap attribute for firstname.
     * 
     * @return the ldap attribute for firstname
     */
    public String getLdapAttrFirstname() {
        return this.ldapAttrFirstname;
    }

    /**
     * Sets the ldap attribute for firstname.
     * 
     * @param ldapAttrFirstname
     *            the new ldap attribute for firstname
     */
    public void setLdapAttrFirstname(final String ldapAttrFirstname) {
        this.ldapAttrFirstname = ldapAttrFirstname;
    }

}

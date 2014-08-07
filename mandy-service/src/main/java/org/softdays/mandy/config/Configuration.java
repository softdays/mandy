package org.softdays.mandy.config;


public class Configuration {

    private String contextRoot = "mandy";

    private String ldapAttributeForLastname = "sn";

    private String ldapAttributeForFirstname = "givenName";

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

}

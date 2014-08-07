package org.softdays.mandy.dto;

public class ResourceDto {

    private Long resourceId;

    private String login;

    private String lastName;

    private String firstName;

    private String role;

    public Long getResourceId() {
	return resourceId;
    }

    public void setResourceId(Long resourceId) {
	this.resourceId = resourceId;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

}

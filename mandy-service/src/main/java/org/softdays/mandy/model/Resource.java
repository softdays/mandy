package org.softdays.mandy.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Représente une personne en capacité d'imputer sur des activités.
 * 
 * @author rpatriarche
 */
@Entity
@Table(name = "RESOURCE")
public class Resource extends AbstractEntity {

    public enum Role {
	ROLE_USER, ROLE_MANAGER, ROLE_ADMIN;

	public String getName() {
	    return this.name();
	}
    }

    /**
     * Identifiant LDAP.
     */
    @Column(nullable = false, length = 25)
    private String uid;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(columnDefinition = "varchar(25) not null default 'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "resource")
    private Set<Imputation> imputations;

    @ManyToMany
    @JoinTable(name = "ACTIVITY_RESOURCE", joinColumns = { @JoinColumn(name = "RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACTIVITY_ID") })
    @org.hibernate.annotations.ForeignKey(name = "FK__ACTRES__RESOURCE_ID", inverseName = "FK__ACTRES__ACTIVITY_ID")
    // for hbm2ddl
    private Set<Activity> activities;

    public Resource() {
	super();
    }

    public Resource(String uid, String lastname, String firstname) {
	this();
	this.uid = uid;
	this.lastName = lastname;
	this.firstName = firstname;
	this.role = Role.ROLE_USER;
    }

    public String getUid() {
	return uid;
    }

    public void setUid(String uid) {
	this.uid = uid;
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

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public Set<Imputation> getImputations() {
	return imputations;
    }

    public void setImputations(Set<Imputation> imputations) {
	this.imputations = imputations;
    }

    public Set<Activity> getActivities() {
	return activities;
    }

    public void setActivities(Set<Activity> activities) {
	this.activities = activities;
    }

}

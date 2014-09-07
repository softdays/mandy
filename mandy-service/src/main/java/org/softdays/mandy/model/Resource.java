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
 * @since 1.0.0
 */
@Entity
@Table(name = "RESOURCE")
public class Resource extends AbstractEntity {

    /**
     * The Enum Role.
     * 
     * @author rpatriarche
     * @since 1.0.0
     */
    public enum Role {
	ROLE_USER, ROLE_MANAGER, ROLE_ADMIN;

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
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
    @JoinTable(name = "TEAM_RESOURCE", joinColumns = { @JoinColumn(name = "RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEAM_ID") })
    @org.hibernate.annotations.ForeignKey(name = "FK__TEAM_RESOURCE__RESOURCE", inverseName = "FK__TEAM_RESOURCE__TEAM")
    // for hbm2ddl
    private Set<Team> teams;

    /**
     * Instantiates a new resource.
     */
    public Resource() {
	super();
    }

    /**
     * Instantiates a new resource.
     * 
     * @param uid
     *            the uid
     * @param lastname
     *            the lastname
     * @param firstname
     *            the firstname
     */
    public Resource(String uid, String lastname, String firstname) {
	this();
	this.uid = uid;
	this.lastName = lastname;
	this.firstName = firstname;
	this.role = Role.ROLE_USER;
    }

    /**
     * Gets the uid.
     * 
     * @return the uid
     */
    public String getUid() {
	return uid;
    }

    /**
     * Sets the uid.
     * 
     * @param uid
     *            the new uid
     */
    public void setUid(String uid) {
	this.uid = uid;
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
    public Role getRole() {
	return role;
    }

    /**
     * Sets the role.
     * 
     * @param role
     *            the new role
     */
    public void setRole(Role role) {
	this.role = role;
    }

    /**
     * Gets the teams.
     * 
     * @return the teams
     */
    public Set<Team> getTeams() {
	return teams;
    }

    /**
     * Sets the teams.
     * 
     * @param teams
     *            the new teams
     */
    public void setTeams(Set<Team> teams) {
	this.teams = teams;
    }

    /**
     * Gets the imputations.
     * 
     * @return the imputations
     */
    public Set<Imputation> getImputations() {
	return imputations;
    }

    /**
     * Sets the imputations.
     * 
     * @param imputations
     *            the new imputations
     */
    public void setImputations(Set<Imputation> imputations) {
	this.imputations = imputations;
    }

}

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
package org.softdays.mandy.core.model;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.softdays.mandy.core.BaseIdentifiable;
import org.softdays.mandy.core.CoreConstants;

/**
 * Représente une personne en capacité d'imputer sur des activités.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Entity
@Table(name = "RESOURCE")
public class Resource extends BaseIdentifiable {

    private static final long serialVersionUID = 1L;

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
    @Column(nullable = false, length = CoreConstants.DB_UID_LENGTH)
    private String uid;

    @Column(nullable = false, length = CoreConstants.DB_NAME_LENGTH)
    private String lastName;

    @Column(nullable = false, length = CoreConstants.DB_NAME_LENGTH)
    private String firstName;

    @Column(columnDefinition = "varchar(25) not null default 'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "resource")
    private Set<Imputation> imputations;

    @ManyToMany
    @JoinTable(name = "TEAM_RESOURCE", joinColumns = { @JoinColumn(
            name = "RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(
            name = "TEAM_ID") })
    @org.hibernate.annotations.ForeignKey(
            name = "FK__TEAM_RESOURCE__RESOURCE",
            inverseName = "FK__TEAM_RESOURCE__TEAM")
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
    public Resource(final String uid, final String lastname,
            final String firstname) {
        this();
        this.uid = uid;
        this.lastName = lastname;
        this.firstName = firstname;
        this.role = Role.ROLE_USER;
    }

    public Resource(final Long id) {
        this.setId(id);
    }

    /**
     * Gets the uid.
     * 
     * @return the uid
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * Sets the uid.
     * 
     * @param uid
     *            the new uid
     */
    public void setUid(final String uid) {
        this.uid = uid;
    }

    /**
     * Gets the last name.
     * 
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets the last name.
     * 
     * @param lastName
     *            the new last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets the first name.
     * 
     * @param firstName
     *            the new first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the role.
     * 
     * @return the role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Sets the role.
     * 
     * @param role
     *            the new role
     */
    public void setRole(final Role role) {
        this.role = role;
    }

    /**
     * Gets the teams.
     * 
     * @return the teams
     */
    public Set<Team> getTeams() {
        return this.teams;
    }

    /**
     * Sets the teams.
     * 
     * @param teams
     *            the new teams
     */
    public void setTeams(final Set<Team> teams) {
        this.teams = teams;
    }

    /**
     * Gets the imputations.
     * 
     * @return the imputations
     */
    public Set<Imputation> getImputations() {
        return this.imputations;
    }

    /**
     * Sets the imputations.
     * 
     * @param imputations
     *            the new imputations
     */
    public void setImputations(final Set<Imputation> imputations) {
        this.imputations = imputations;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(this.getUid()).append(this.getLastName())
                .append(this.getFirstName()).append(this.getRole())
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        Boolean status = this.equalsConsideringTechnicalLogic(obj);
        if (status == null) {
            final Resource rhs = (Resource) obj;

            status =
                    new EqualsBuilder().appendSuper(this.equals(obj))
                            .append(this.getUid(), rhs.getUid())
                            .append(this.getLastName(), rhs.getLastName())
                            .append(this.getFirstName(), rhs.getFirstName())
                            .append(this.getRole(), rhs.getRole()).isEquals();
        }
        return status;
    }

}

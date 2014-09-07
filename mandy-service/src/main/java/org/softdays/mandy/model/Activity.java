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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

/**
 * Une activité est l'objet de l'imputation d'une charge.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Entity
@Table(name = "ACTIVITY")
public class Activity extends AbstractEntity {

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 10)
    private String shortLabel;

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 50)
    private String longLabel;

    @Column(columnDefinition = "char(1) not null default 'P'")
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private Integer position;

    @ManyToMany(mappedBy = "activities")
    private Set<Team> teams;

    @OneToMany(mappedBy = "activity")
    private Set<Imputation> imputations;

    /**
     * Instantiates a new activity.
     */
    public Activity() {
	super();
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public ActivityType getType() {
	return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the new type
     */
    public void setType(ActivityType type) {
	this.type = type;
    }

    /**
     * Gets the short label.
     * 
     * @return the short label
     */
    public String getShortLabel() {
	return shortLabel;
    }

    /**
     * Sets the short label.
     * 
     * @param shortLabel
     *            the new short label
     */
    public void setShortLabel(String shortLabel) {
	this.shortLabel = shortLabel;
    }

    /**
     * Gets the long label.
     * 
     * @return the long label
     */
    public String getLongLabel() {
	return longLabel;
    }

    /**
     * Sets the long label.
     * 
     * @param longLabel
     *            the new long label
     */
    public void setLongLabel(String longLabel) {
	this.longLabel = longLabel;
    }

    /**
     * Gets the position.
     * 
     * @return the position
     */
    public Integer getPosition() {
	return position;
    }

    /**
     * Sets the position.
     * 
     * @param position
     *            the new position
     */
    public void setPosition(Integer position) {
	this.position = position;
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

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

/**
 * The Class Team.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Entity
@Table(name = "TEAM")
public class Team extends AbstractEntity {

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 10)
    private String code;

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 50)
    private String label;

    @ManyToMany
    @JoinTable(name = "ACTIVITY_TEAM", joinColumns = { @JoinColumn(name = "TEAM_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACTIVITY_ID") })
    @org.hibernate.annotations.ForeignKey(name = "FK__ACTIVITY_TEAM__TEAM", inverseName = "FK__ACTIVITY_TEAM__ACTIVITY")
    // for hbm2ddl
    private Set<Activity> activities;

    @ManyToMany(mappedBy = "teams")
    private Set<Resource> resources;

    /**
     * Gets the code.
     * 
     * @return the code
     */
    public String getCode() {
	return code;
    }

    /**
     * Sets the code.
     * 
     * @param code
     *            the new code
     */
    public void setCode(String code) {
	this.code = code;
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
	return label;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            the new label
     */
    public void setLabel(String label) {
	this.label = label;
    }

    /**
     * Gets the activities.
     * 
     * @return the activities
     */
    public Set<Activity> getActivities() {
	return activities;
    }

    /**
     * Sets the activities.
     * 
     * @param activities
     *            the new activities
     */
    public void setActivities(Set<Activity> activities) {
	this.activities = activities;
    }

    /**
     * Gets the resources.
     * 
     * @return the resources
     */
    public Set<Resource> getResources() {
	return resources;
    }

    /**
     * Sets the resources.
     * 
     * @param resources
     *            the new resources
     */
    public void setResources(Set<Resource> resources) {
	this.resources = resources;
    }

}

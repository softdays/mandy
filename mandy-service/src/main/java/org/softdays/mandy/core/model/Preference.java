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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.softdays.mandy.core.BaseEqualable;

/**
 * The user preferences entity.
 * 
 * @author rpatriarche
 * @since 1.1.0
 * 
 */
@Entity
@Table(name = "PREFERENCE")
public class Preference extends BaseEqualable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RESOURCE_ID")
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "RESOURCE_ID")
    @org.hibernate.annotations.ForeignKey(name = "FK__PREFERENCES__RESOURCE")
    private Resource resource;

    /**
     * The user preference for imputation granularity. Must be multiple of 1.
     */
    @Column(columnDefinition = "float NOT NULL default '0.5'")
    private Float granularity = Quota.HALF.floatValue();

    /**
     * Mapping comments:
     * 
     * - Using OrderColumn annotation implies teh creation of a composite
     * primary key on (preference_id, activity_order) causes by the List
     * semantic.
     * 
     * - No way to complete this PK with an unique constraint on (preference_id,
     * activity_id) because when you reorder the list and ask Hibernate to
     * persist your changes, Hibernate will do one udpdate per ordered item to
     * persist the new list order to the database (one update per item will be
     * performed). You will get "ERROR: Unique index or primary key violation"
     * 
     * @see https://forum.hibernate.org/viewtopic.php?f=1&t=924496
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PREFERENCE_ACTIVITY",
            // uniqueConstraints = @UniqueConstraint(
            // name = "UQ__PREFERENCE_ACTIVITY__PREFERENCE_ID__ACTIVITY_ID",
            // columnNames = { "PREFERENCE_ID", "ACTIVITY_ID" }),
            joinColumns = { @JoinColumn(
                    name = "PREFERENCE_ID",
                    foreignKey = @ForeignKey(
                            name = "FK__PREFERENCE_ACTIVITY__PREFERENCE_ID")) },
            inverseJoinColumns = { @JoinColumn(
                    name = "ACTIVITY_ID",
                    foreignKey = @ForeignKey(
                            name = "FK__PREFERENCE_ACTIVITY__ACTIVITY_ID")) })
    @org.hibernate.annotations.ForeignKey(
            name = "FK__PREFERENCE_ACTIVITY__PREFERENCE",
            inverseName = "FK__PREFERENCE_ACTIVITY__ACTIVITY")
    @OrderColumn(name = "ACTIVITY_ORDER")
    private List<Activity> filteredActivities;

    public Preference() {
        super();
    }

    public Preference(final Resource resource) {
        this();
        this.setId(resource.getId());
        this.resource = resource;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
        this.resource = id == null ? null : new Resource(id);
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(final Resource resource) {
        this.resource = resource;
        this.id = resource == null ? null : resource.getId();
    }

    public Float getGranularity() {
        return this.granularity;
    }

    public void setGranularity(final Float granularity) {
        this.granularity = granularity;
    }

    public List<Activity> getFilteredActivities() {
        return this.filteredActivities;
    }

    public void setFilteredActivities(final List<Activity> filteredActivities) {
        this.filteredActivities = filteredActivities;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(this.getResource()).append(this.getGranularity())
                .append(this.getFilteredActivities()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        Boolean status = this.equalsConsideringTechnicalLogic(obj);
        if (status == null) {
            final Preference pref = (Preference) obj;
            status =
                    new EqualsBuilder()
                            .appendSuper(this.equals(obj))
                            .append(this.getResource(), pref.getResource())
                            .append(this.getGranularity(),
                                    pref.getGranularity())
                            .append(this.getFilteredActivities(),
                                    pref.getFilteredActivities()).isEquals();
        }
        return status;
    }

}

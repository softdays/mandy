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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.softdays.mandy.core.BaseEntity;
import org.softdays.mandy.core.CoreConstants;

/**
 * Une imputation permet de tracer la charge des ressources sur les activités.
 * Contrainte unique : une seule imputation possible pour une activité, une
 * ressource et une date donnée.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Entity
@Table(name = "IMPUTATION", uniqueConstraints = @UniqueConstraint(
        columnNames = { "ACTIVITY_ID", "RESOURCE_ID", "DATE" }))
public class Imputation extends BaseEntity {

    @ManyToOne
    @JoinColumn(
            name = "ACTIVITY_ID",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "FK__IMPUTATION__ACTIVITY"))
    @org.hibernate.annotations.ForeignKey(name = "FK__IMPUTATION__ACTIVITY")
    // for hbm2ddl
    private Activity activity;

    @ManyToOne
    @JoinColumn(
            name = "RESOURCE_ID",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "FK__IMPUTATION__RESOURCE"))
    @org.hibernate.annotations.ForeignKey(name = "FK__IMPUTATION__RESOURCE")
    // for hbm2ddl
    private Resource resource;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private Float quota;

    @Column(length = CoreConstants.DB_DESCRIPTION_LENGTH)
    private String comment;

    /**
     * Instantiates a new imputation.
     */
    public Imputation() {
        super();
    }

    /**
     * Instantiates a new imputation.
     * 
     * @param imputationId
     *            the imputation id
     */
    public Imputation(final Long imputationId) {
        this();
        this.setId(imputationId);
    }

    /**
     * Gets the activity.
     * 
     * @return the activity
     */
    public Activity getActivity() {
        return this.activity;
    }

    /**
     * Sets the activity.
     * 
     * @param activity
     *            the new activity
     */
    public void setActivity(final Activity activity) {
        this.activity = activity;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
        return (Date) this.date.clone();
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            the new date
     */
    public void setDate(final Date date) {
        this.date = (Date) date.clone();
    }

    /**
     * Gets the resource.
     * 
     * @return the resource
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * Sets the resource.
     * 
     * @param resource
     *            the new resource
     */
    public void setResource(final Resource resource) {
        this.resource = resource;
    }

    /**
     * Gets the quota.
     * 
     * @return the quota
     */
    public Float getQuota() {
        return this.quota;
    }

    /**
     * Sets the quota.
     * 
     * @param quota
     *            the new quota
     */
    public void setQuota(final Float quota) {
        this.quota = quota;
    }

    /**
     * Gets the comment.
     * 
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Sets the comment.
     * 
     * @param comment
     *            the new comment
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(this.getActivity()).append(this.getComment())
                .append(this.getDate()).append(this.getQuota())
                .append(this.getResource()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        Boolean status = this.equalsConsideringTechnicalLogic(obj);
        if (status == null) {
            final Imputation rhs = (Imputation) obj;
            status = new EqualsBuilder().appendSuper(this.equals(obj))
                    .append(this.getActivity(), rhs.getActivity())
                    .append(this.getComment(), rhs.getComment())
                    .append(this.getDate(), rhs.getDate()).isEquals();
        }
        return status;
    }
}
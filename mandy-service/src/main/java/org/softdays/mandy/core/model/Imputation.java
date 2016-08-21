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

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.softdays.mandy.core.DefaultIdentifiable;
import org.softdays.mandy.core.CoreConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Une imputation permet de tracer la charge des ressources sur les activités.
 * Contrainte unique : une seule imputation possible pour une activité, une
 * ressource et une date donnée.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "IMPUTATION", uniqueConstraints = @UniqueConstraint(
        columnNames = { "ACTIVITY_ID", "RESOURCE_ID", "DATE" }, name = "UK__IMPUTATION"))
public class Imputation extends DefaultIdentifiable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "FK__IMPUTATION__ACTIVITY"))
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID", updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "FK__IMPUTATION__RESOURCE"))
    // for hbm2ddl
    private Resource resource;

    @Column(name = "DATE", nullable = false, updatable = false)
    @Type(type = "org.hibernate.type.LocalDateType")
    private LocalDate date;

    @Column(name = "QUOTA", nullable = false)
    private Float quota;

    @Column(name = "COMMENT", length = CoreConstants.DB_DESCRIPTION_LENGTH)
    private String comment;

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

    @Override
    protected void businessHashCode(final HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getActivity()).append(this.getComment()).append(this.getDate())
                .append(this.getQuota()).append(this.getResource()).toHashCode();
    }

    @Override
    protected void businessEquals(final Object obj, final EqualsBuilder equalsBuilder) {
        final Imputation i = (Imputation) obj;
        equalsBuilder.append(this.getActivity(), i.getActivity())
                .append(this.getComment(), i.getComment()).append(this.getDate(), i.getDate());

    }

}

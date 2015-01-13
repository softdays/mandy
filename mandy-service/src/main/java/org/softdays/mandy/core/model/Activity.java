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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;
import org.softdays.mandy.core.BaseIdentifiable;
import org.softdays.mandy.core.CoreConstants;

/**
 * Une activité est l'objet de l'imputation d'une charge.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Entity
@Table(name = "ACTIVITY")
public class Activity extends BaseIdentifiable {

    private static final long serialVersionUID = 1L;

    @NaturalId(mutable = true)
    @Column(nullable = false, length = CoreConstants.DB_SHORT_LABEL_LENGTH)
    private String shortLabel;

    @NaturalId(mutable = true)
    @Column(nullable = false, length = CoreConstants.DB_LONG_LABEL_LENGTH)
    private String longLabel;

    @Column(columnDefinition = "char(1) not null default 'P'")
    @Enumerated(EnumType.STRING)
    /**
     * @see org.softdays.mandy.model.ActivityType
     */
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
        return this.type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the new type
     */
    public void setType(final ActivityType type) {
        this.type = type;
    }

    /**
     * Gets the short label.
     * 
     * @return the short label
     */
    public String getShortLabel() {
        return this.shortLabel;
    }

    /**
     * Sets the short label.
     * 
     * @param shortLabel
     *            the new short label
     */
    public void setShortLabel(final String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * Gets the long label.
     * 
     * @return the long label
     */
    public String getLongLabel() {
        return this.longLabel;
    }

    /**
     * Sets the long label.
     * 
     * @param longLabel
     *            the new long label
     */
    public void setLongLabel(final String longLabel) {
        this.longLabel = longLabel;
    }

    /**
     * Gets the position.
     * 
     * @return the position
     */
    public Integer getPosition() {
        return this.position;
    }

    /**
     * Sets the position.
     * 
     * @param position
     *            the new position
     */
    public void setPosition(final Integer position) {
        this.position = position;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(this.getLongLabel()).append(this.getShortLabel())
                .append(this.getPosition()).append(this.getType()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        Boolean status = this.equalsConsideringTechnicalLogic(obj);
        if (status == null) {
            final Activity rhs = (Activity) obj;
            status =
                    new EqualsBuilder().appendSuper(this.equals(obj))
                            .append(this.getShortLabel(), rhs.getShortLabel())
                            .append(this.getLongLabel(), rhs.getLongLabel())
                            .append(this.getPosition(), rhs.getPosition())
                            .append(this.getType(), rhs.getType()).isEquals();
        }
        return status;
    }
}

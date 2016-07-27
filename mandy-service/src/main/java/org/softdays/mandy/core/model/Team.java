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
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.softdays.mandy.core.BaseIdentifiable;
import org.softdays.mandy.core.CoreConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class Team.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TEAM",
        uniqueConstraints = @UniqueConstraint(columnNames = { "CODE", "LABEL" }, name = "UK__TEAM"))
public class Team extends BaseIdentifiable {

    private static final long serialVersionUID = 1L;

    @Column(name = "CODE", nullable = false, length = CoreConstants.DB_SHORT_LABEL_LENGTH)
    private String code;

    @Column(name = "LABEL", nullable = false, length = CoreConstants.DB_LONG_LABEL_LENGTH)
    private String label;

    @ManyToMany
    @JoinTable(name = "ACTIVITY_TEAM", joinColumns = { @JoinColumn(name = "TEAM_ID") },
            inverseJoinColumns = { @JoinColumn(name = "ACTIVITY_ID") },
            foreignKey = @ForeignKey(name = "FK__ACTIVITY_TEAM__TEAM"),
            inverseForeignKey = @ForeignKey(name = "FK__ACTIVITY_TEAM__ACTIVITY"))
    private Set<Activity> activities;

    @ManyToMany(mappedBy = "teams")
    private Set<Resource> resources;

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getCode())
                .append(this.getLabel()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        Boolean status = this.equalsConsideringTechnicalLogic(obj);
        if (status == null) {
            final Team rhs = (Team) obj;

            status = new EqualsBuilder().appendSuper(this.equals(obj))
                    .append(this.getCode(), rhs.getCode()).append(this.getLabel(), rhs.getLabel())
                    .isEquals();
        }
        return status;
    }
}

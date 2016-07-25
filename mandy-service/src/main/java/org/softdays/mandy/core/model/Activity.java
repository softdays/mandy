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
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.softdays.mandy.core.BaseIdentifiable;
import org.softdays.mandy.core.CoreConstants;
import org.softdays.mandy.core.converter.ActivityCategoryConverter;
import org.softdays.mandy.core.converter.ActivityTypeConverter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the object of the imputation.
 * 
 * <p>
 * Business rules:
 * <ul>
 * <li>a typed activity MUST have a not null parent activity</li>
 * <li>an untyped (unspecified) activity remains imputable</li>
 * <li>si un utilisateur saisit une imputation sur une activité parent avec des sous-activités alors
 * il ne peut plus saisir d'imputation sur les sous-activités de cette activité parente. S'il veut
 * finalelment ventiler ses imputations sur les sous-activités de l'activité parente, il doit
 * d'abord supprimer l'imputation sur l'activité parente ou passer par la fonction de ventilation
 * d'une imputation de l'activité parente sur ces sous-activités</li>
 * <li>si un utilisateur saisit une imputation sur une sous-activité il ne lui est plus possible
 * d'imputer au niveau de l'activité parente sauf s'il supprime toutes les imputations existantes
 * sur les sous-activités de cette activité parente ou qu'il utilise la fonction de réagrégation des
 * imputations des sous-activités sur leur activité parente.</li>
 * <li></li>
 * <li></li>
 * </ul>
 * </p>
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ACTIVITY",
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "SHORT_LABEL", "LONG_LABEL", "CATEGORY", "TYPE", "POSITION" },
                name = "UK__ACTIVITY"))
public class Activity extends BaseIdentifiable {

    private static final long serialVersionUID = 1L;

    @Column(name = "SHORT_LABEL", nullable = false, length = CoreConstants.DB_SHORT_LABEL_LENGTH)
    private String shortLabel;

    @Column(name = "LONG_LABEL", nullable = false, length = CoreConstants.DB_LONG_LABEL_LENGTH)
    private String longLabel;

    /**
     * @see org.softdays.mandy.ActivityCategory
     */
    @Column(name = "CATEGORY", columnDefinition = "char(1) not null default 'P'")
    @Convert(converter = ActivityCategoryConverter.class)
    private ActivityCategory category;

    /**
     * @see org.softdays.mandy.ActivityType
     */
    @Column(name = "TYPE", columnDefinition = "char(1) not null default 'U'")
    @Convert(converter = ActivityTypeConverter.class)
    private ActivityType type;

    @Column(name = "POSITION", nullable = false)
    private Integer position;

    @ManyToMany(mappedBy = "activities")
    private Set<Team> teams;

    @OneToMany(mappedBy = "activity")
    private Set<Imputation> imputations;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID", updatable = false, nullable = true,
            foreignKey = @ForeignKey(name = "FK__ACTIVITY__PARENT_ID"))
    private Activity parentActivity;

    public Activity(final Long id) {
        this();
        this.setId(id);
    }

    public Activity(final Activity parent) {
        super();
        this.parentActivity = parent;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getLongLabel())
                .append(this.getShortLabel()).append(this.getPosition()).append(this.getCategory())
                .append(this.getType()).append(this.getParentActivity()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        Boolean status = this.equalsConsideringTechnicalLogic(obj);
        if (status == null) {
            final Activity rhs = (Activity) obj;
            status = new EqualsBuilder().appendSuper(this.equals(obj))
                    .append(this.getShortLabel(), rhs.getShortLabel())
                    .append(this.getLongLabel(), rhs.getLongLabel())
                    .append(this.getPosition(), rhs.getPosition())
                    .append(this.getCategory(), rhs.getCategory())
                    .append(this.getType(), rhs.getType()).isEquals();
        }
        return status;
    }
}

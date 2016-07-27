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
package org.softdays.mandy.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The activity DTO.
 * 
 * @author rpatriarche
 * @since 1.0.0
 * @version 1.3.0 Rename type by category and add new type
 */
@Getter
@Setter
@NoArgsConstructor
public class ActivityDto {

    private Long id;

    private String shortLabel;

    private String longLabel;

    private Character category;

    private Character type;

    private Long parentActivityId;

    /**
     * Doit être surchargée en cohérence avec la surcharge de equals().
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getId())
                .toHashCode();
    }

    /**
     * Surcharge nécessaire si utilisation au sein d'une LinkedhashMap.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final ActivityDto other = (ActivityDto) obj;

        return new EqualsBuilder().appendSuper(this.equals(obj)).append(this.getId(), other.getId())
                .isEquals();
    }

    @Override
    public String toString() {
        return "ActivityDto [id=" + this.id + ", shortLabel=" + this.shortLabel + ", longLabel="
                + this.longLabel + ", category=" + this.category + ", type=" + this.type + "]";
    }

}

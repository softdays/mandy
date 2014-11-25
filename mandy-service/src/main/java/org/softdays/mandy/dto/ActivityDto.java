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

/**
 * The activity DTO.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class ActivityDto {

    private Long id;

    private String shortLabel;

    private String longLabel;

    private String type;

    /**
     * Instantiates a new activity dto.
     */
    public ActivityDto() {
        super();
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the new type
     */
    public void setType(final String type) {
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
     * Doit être surchargée en cohérence avec la surcharge de equals().
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(this.getId()).toHashCode();
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

        return new EqualsBuilder().appendSuper(this.equals(obj))
                .append(this.getId(), other.getId()).isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ActivityDto [id=" + this.id + ", shortLabel=" + this.shortLabel
                + ", type=" + this.type + "]";
    }

}

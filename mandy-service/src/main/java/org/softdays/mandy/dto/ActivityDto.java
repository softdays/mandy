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
package org.softdays.mandy.dto;

/**
 * Created by rpatriarche on 09/03/14.
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
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the new type
     */
    public void setType(String type) {
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    // nécessaire si utilisation au sein d'une LinkedhashMap
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ActivityDto other = (ActivityDto) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ActivityDto [id=" + id + ", shortLabel=" + shortLabel
		+ ", type=" + type + "]";
    }

}

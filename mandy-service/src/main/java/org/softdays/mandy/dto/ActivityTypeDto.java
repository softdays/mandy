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
 * The Class ActivityTypeDto.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class ActivityTypeDto {

    private String name;

    private String description;

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the description.
     * 
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TypeActivityDto [name=" + name + "]";
    }

}

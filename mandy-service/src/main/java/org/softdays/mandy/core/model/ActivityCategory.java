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

import org.softdays.mandy.core.PersistentEnum;

/**
 * Activity categories.
 * 
 * @author rpatriarche
 * 
 * @since 1.3.0
 */
public enum ActivityCategory implements PersistentEnum {

    PROJECT('P', "Project"),

    ABSENCE('A', "Absence"),

    OTHER('O', "Other");

    private Character id;

    private String description;

    /**
     * Instantiates a new activity type.
     * 
     * @param desc
     *            the desc
     */
    ActivityCategory(final Character id, final String desc) {
        this.id = id;
        this.description = desc;
    }

    /**
     * /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name();
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public Character getId() {
        return this.id;
    }

}

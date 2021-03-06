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

import org.softdays.commons.jpa.converter.GenericPersistentEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * Activity categories.
 * 
 * @author rpatriarche
 * 
 * @since 1.3.0
 */
@Getter
public enum ActivityCategory implements GenericPersistentEnum<Character> {

    PROJECT('P', "Project"),

    ABSENCE('A', "Absence"),

    OTHER('O', "Other");

    @Setter
    private Character pk;

    @Setter
    private String description;

    /**
     * Instantiates a new activity type.
     * 
     * @param desc
     *            the desc
     */
    ActivityCategory(final Character pk, final String desc) {
        this.pk = pk;
        this.description = desc;
    }

    /**
     * Returns an {@link ActivityCategory} from code.
     *
     * @param code
     *            the code of the activity category
     * @return the {@link ActivityCategory} matching given code
     */
    public static ActivityCategory fromCode(final Character code) {
        if (code == null) {
            return null;
        }
        switch (code) {
            case 'P':
                return PROJECT;

            case 'A':
                return ABSENCE;

            case 'O':
                return OTHER;

            default:
                throw new IllegalArgumentException("Code <" + code + "> not supported");
        }
    }

}

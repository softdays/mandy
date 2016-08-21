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
 * Describes activity types.
 * 
 * This enum has been redesigned in version 1.3 to allow finer granularity of
 * imputation.
 * 
 * The new {@link ActivityCategory} enum replaces the previous role of
 * ActivityType.
 * 
 * @author repatriarche
 * 
 * @since 1.0.0
 * 
 * @version 1.3.0
 *
 */
@Getter
public enum ActivityType implements GenericPersistentEnum<Character> {

    ANA('A', "Analysis/Design"),

    MOD('M', "Modeling"),

    FIX('F', "Fix/Defect"),

    EVO('E', "Evolution"),

    INT('I', "Configuration/Integration"),

    REU('C', "Coordination/Meeting"),

    UNS('U', "Unspecified");

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
    ActivityType(final Character pk, final String desc) {
        this.pk = pk;
        this.description = desc;
    }

    /**
     * Returns an {@link ActivityType} from code.
     *
     * @param code
     *            the code of the activity type
     * @return the {@link ActivityType} matching given code.
     */
    public static ActivityType fromCode(final Character code) {
        ActivityType type;

        switch (code) {
            case 'A':
                type = ActivityType.ANA;
            break;

            case 'M':
                type = ActivityType.MOD;
            break;

            case 'F':
                type = ActivityType.FIX;
            break;

            case 'E':
                type = ActivityType.EVO;
            break;

            case 'I':
                type = ActivityType.INT;
            break;

            case 'C':
                type = ActivityType.REU;
            break;

            case 'U':
                type = ActivityType.UNS;
            break;

            default:
                throw new IllegalArgumentException("Code <" + code + "> not supported");
        }

        return type;
    }

}

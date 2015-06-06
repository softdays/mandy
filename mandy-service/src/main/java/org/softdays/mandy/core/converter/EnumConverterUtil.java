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
package org.softdays.mandy.core.converter;

import org.softdays.mandy.core.PersistentEnum;

/**
 * Helps to retrieve Enum from id.
 * 
 * @author repatriarche
 * 
 * @since 1.3.0
 */
public final class EnumConverterUtil {

    private EnumConverterUtil() {}

    public static <T extends Enum<T> & PersistentEnum> T valueOf(final Class<T> persistentClass,
            final Character id) {
        for (T e : persistentClass.getEnumConstants()) {
            if (e.getId().equals(id)) {

                return e;
            }
        }

        throw new IllegalArgumentException("No enum const " + persistentClass.getName()
                + " for id \'" + id + "\'");
    }

}

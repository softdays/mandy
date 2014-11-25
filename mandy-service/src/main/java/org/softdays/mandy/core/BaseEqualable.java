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
package org.softdays.mandy.core;

/**
 * This base class allows to compare to objects in terms of technical and
 * business logic.
 * 
 * @author rpatriarche
 * @since 1.0.1
 */
public class BaseEqualable implements Equalable {

    /**
     * Instantiates a new abstract equalable.
     */
    public BaseEqualable() {
        super();
    }

    @Override
    public final Boolean equalsConsideringTechnicalLogic(final Object obj) {
        if (obj == null) {
            return Boolean.FALSE;
        }
        if (obj == this) {
            return Boolean.TRUE;
        }

        return obj.getClass() == this.getClass() ? null : Boolean.FALSE;
    }

}

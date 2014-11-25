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
 * The Interface Equalable.
 * 
 * @author rpatriarche
 * @since 1.0.1
 */
public interface Equalable {

    /**
     * Returns true or false if the basic checks provides somes absolute
     * certainty about equality between objetcs, null if another checks are
     * mandatory.
     * 
     * @param obj
     *            the object to which compare equality.
     * 
     * @return a tri-state Boolean object. True if object is the same object,
     *         false if object is null or not same type and null if objet is
     *         same type.
     */
    Boolean equalsConsideringTechnicalLogic(Object obj);

}

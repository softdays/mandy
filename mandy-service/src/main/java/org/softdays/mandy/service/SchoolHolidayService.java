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
package org.softdays.mandy.service;

import org.joda.time.DateTime;

/**
 * Provided by Index-Education.
 * http://telechargement.index-education.com/vacances.xml
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public interface SchoolHolidayService {

    /**
     * Checks if is school holiday.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is school holiday
     */
    boolean isSchoolHoliday(DateTime givenDate);

}

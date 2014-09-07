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
package org.softdays.mandy.service;

import java.text.ParseException;
import java.util.Date;

import org.softdays.mandy.dto.calendar.DataGridDto;

/**
 * Calendar service provides all stuff required to build an monthly input grid.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public interface CalendarService {

    /**
     * Returns the calendar grid of the given date.
     * 
     * @param givenDate
     *            the given date
     * @return the data grid of the month
     */
    DataGridDto getDataGridOfTheMonth(Date givenDate);

    /**
     * Returns the first monday of the month to which belongs the given date.
     * 
     * @param givenDate
     *            the given date
     * @return the first monday of month
     */
    Date getFirstMondayOfMonth(Date givenDate);

    /**
     * Returns the first sunday after the end of the month to which belongs the
     * given date.
     * 
     * @param givenDate
     *            the given date
     * @return the first sunday after end of month
     */
    Date getFirstSundayAfterEndOfMonth(Date givenDate);

    /**
     * Returns the first day of the given month of the given year.
     * 
     * @param year
     *            A year string (format = yyyy)
     * @param month
     *            A string denoting a month (format = MM)
     * @return A Date.
     * 
     * @throws ParseException
     *             Lève une exception si les formats ne correspondent pas à une
     *             date.
     */
    Date getFirstDayOfTheMonth(String year, String month) throws ParseException;

}

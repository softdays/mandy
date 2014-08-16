package org.softdays.mandy.service;

import java.text.ParseException;
import java.util.Date;

import org.softdays.mandy.dto.calendar.DataGridDto;

/**
 * Calendar service provides all stuff required to build an monthly input grid.
 * 
 * @author rpatriarche
 */
public interface CalendarService {

    /**
     * Returns the calendar grid of the given date.
     */
    DataGridDto getDataGridOfTheMonth(Date givenDate);

    /**
     * Returns the first monday of the month to which belongs the given date.
     */
    Date getFirstMondayOfMonth(Date givenDate);

    /**
     * Returns the first sunday after the end of the month to which belongs the
     * given date.
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

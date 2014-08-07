package org.softdays.mandy.service;

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

}

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
package org.softdays.mandy.service.support;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.dto.calendar.DayDto.Status;
import org.softdays.mandy.dto.calendar.WeekDto;
import org.softdays.mandy.service.BankHolidayService;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.SchoolHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class CalendarServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
public class CalendarServiceImpl implements CalendarService {

    private static final String FIRST_DAY_OF_THE_MONTH = "01";

    private static final DateTimeFormatter FORMATTER = DateTimeFormat
            .forPattern("yyyyMMdd");

    @Autowired
    private BankHolidayService bankHolidayService;

    @Autowired
    private SchoolHolidayService schoolHolidaysService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getDataGridOfTheMonth(java
     * .util.Date)
     */
    @Override
    public DataGridDto getDataGridOfTheMonth(Date base) {
        // Date base = this.determineBaseDate(givenDate);
        Date start = getFirstMondayOfMonth(base);
        Date end = getFirstSundayAfterEndOfMonth(base);
        Date currentDate = start;
        DataGridDto dataGrid = new DataGridDto(base);
        WeekDto week = dataGrid.newWeek();
        while (!currentDate.equals(end)) {
            if (!isEndOfWeek(currentDate)) {
                // ici on est sûr de devoir insérer un jour ouvré
                if (week.isCompleted()) {
                    // c'est bien ici qu'il faut gérer les créations de semaines
                    week = dataGrid.newWeek();
                }
                week.newDay(currentDate, this.getDateStatus(currentDate));
            }
            currentDate = new DateTime(currentDate).plusDays(1).toDate();
        }
        return dataGrid;
    }

    /**
     * Determine base date.
     * 
     * @param givenDate
     *            the given date
     * @return the date
     */
    protected Date determineBaseDate(Date givenDate) {
        DateTime base = new DateTime(givenDate);
        if (dateWeekContainsOneDayOfPreviousMonth(base)) {
            base = base.minusMonths(1);
        }

        return base.dayOfMonth().withMinimumValue().toDate();
    }

    /**
     * Date week contains one day of previous month.
     * 
     * @param date
     *            the date
     * @return true, if successful
     */
    protected boolean dateWeekContainsOneDayOfPreviousMonth(DateTime date) {
        int week = date.getWeekOfWeekyear();
        int month = date.getMonthOfYear();
        DateTime prev = date.minusDays(1);
        boolean sameMonth = true;
        while (prev.getWeekOfWeekyear() == week && sameMonth) {
            sameMonth = prev.getMonthOfYear() == month;
            prev = prev.minusDays(1);
        }
        return !sameMonth;
    }

    private boolean isMonday(DateTime date) {
        return date.getDayOfWeek() == DateTimeConstants.MONDAY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstDayOfTheMonth(java
     * .lang.String, java.lang.String)
     */
    @Override
    public Date getFirstDayOfTheMonth(String year, String month) {

        return FORMATTER.parseDateTime(year + month + FIRST_DAY_OF_THE_MONTH)
                .toDate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstMondayOfMonth(java
     * .util.Date)
     */
    @Override
    public Date getFirstMondayOfMonth(final Date givenDate) {
        DateTime date = new DateTime(givenDate);
        date = date.dayOfMonth().withMinimumValue();
        while (!isMonday(date)) {
            date = date.plusDays(1);
        }

        return date.toDate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstSundayAfterEndOfMonth
     * (java.util.Date)
     */
    @Override
    public Date getFirstSundayAfterEndOfMonth(final Date givenDate) {
        DateTime date = new DateTime(givenDate);
        date = date.dayOfMonth().withMaximumValue();
        while (date.getDayOfWeek() != DateTimeConstants.SUNDAY) {
            date = date.plusDays(1);
        }

        return date.toDate();
    }

    /**
     * Next.
     * 
     * @param date
     *            the date
     * @return the date
     */
    protected Date next(Date date) {
        return new DateTime(date).plusDays(1).toDate();
    }

    /**
     * Gets the date status.
     * 
     * @param givenDate
     *            the given date
     * @return the date status
     */
    protected Status getDateStatus(Date givenDate) {
        Status status = Status.WD;
        if (isBankHoliday(givenDate)) {
            status = Status.BH;
        } else if (isSchoolHoliday(givenDate)) {
            status = Status.SH;
        } else if (isEndOfWeek(givenDate)) {
            status = Status.WE;
        }

        return status;
    }

    /**
     * Checks if is school holiday.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is school holiday
     */
    protected boolean isSchoolHoliday(Date givenDate) {
        return schoolHolidaysService.isSchoolHoliday(givenDate);
    }

    /**
     * Checks if is end of week.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is end of week
     */
    protected boolean isEndOfWeek(Date givenDate) {
        DateTime date = new DateTime(givenDate);
        int dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DateTimeConstants.SUNDAY
                || dayOfWeek == DateTimeConstants.SATURDAY;
    }

    /**
     * Checks if is bank holiday.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is bank holiday
     */
    protected boolean isBankHoliday(Date givenDate) {
        String bankHolidaySummary = bankHolidayService
                .getBankHolidaySummary(givenDate);
        return bankHolidaySummary != null;
    }
}
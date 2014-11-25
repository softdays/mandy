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
package org.softdays.mandy.service.support;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableDateTime;
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

    private static final String FIRST_DAY_OF_MONTH = "01";

    private static final DateTimeFormatter FORMATTER = DateTimeFormat
            .forPattern("yyyyMMdd");

    @Autowired
    private BankHolidayService bankHolidayService;

    @Autowired
    private SchoolHolidayService schoolHolidaysService;

    /**
     * Instantiates a new calendar service impl.
     */
    public CalendarServiceImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getDataGridOfTheMonth(java
     * .util.Date)
     */
    @Override
    public DataGridDto getDataGridOfTheMonth(final DateTime date) {
        DateTime currentDate = this.getFirstMondayOfMonth(date);
        final DateTime end = this.getFirstSundayAfterEndOfMonth(date);
        final DataGridDto dataGrid = new DataGridDto(date.toDate());
        WeekDto week = dataGrid.createWeek();
        while (!currentDate.equals(end)) {
            if (!this.isEndOfWeek(currentDate)) {
                // ici on est sûr de devoir insérer un jour ouvré
                if (week.isCompleted()) {
                    // c'est bien ici qu'il faut gérer les créations de semaines
                    week = dataGrid.createWeek();
                }
                week.createDay(currentDate, this.getDateStatus(currentDate));
            }
            currentDate = currentDate.plusDays(1);
        }
        return dataGrid;
    }

    private boolean isMonday(final ReadableDateTime date) {
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
    public DateTime getFirstDayOfTheMonth(final String year, final String month) {

        return FORMATTER.parseDateTime(year + month + FIRST_DAY_OF_MONTH);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstMondayOfMonth(java
     * .util.Date)
     */
    @Override
    public DateTime getFirstMondayOfMonth(final DateTime givenDate) {
        DateTime date = givenDate.dayOfMonth().withMinimumValue();
        while (!this.isMonday(date)) {
            date = date.plusDays(1);
        }

        return date;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstSundayAfterEndOfMonth
     * (java.util.Date)
     */
    @Override
    public DateTime getFirstSundayAfterEndOfMonth(final DateTime givenDate) {
        DateTime date = givenDate.dayOfMonth().withMaximumValue();
        while (date.getDayOfWeek() != DateTimeConstants.SUNDAY) {
            date = date.plusDays(1);
        }

        return date;
    }

    /**
     * Gets the date status.
     * 
     * @param givenDate
     *            the given date
     * @return the date status
     */
    protected Status getDateStatus(final DateTime givenDate) {
        Status status = Status.WD;
        if (this.isBankHoliday(givenDate)) {
            status = Status.BH;
        } else if (this.isSchoolHoliday(givenDate)) {
            status = Status.SH;
        } else if (this.isEndOfWeek(givenDate)) {
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
    protected boolean isSchoolHoliday(final DateTime givenDate) {
        return this.schoolHolidaysService.isSchoolHoliday(givenDate);
    }

    /**
     * Checks if is end of week.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is end of week
     */
    protected boolean isEndOfWeek(final ReadableDateTime date) {
        final int dayOfWeek = date.getDayOfWeek();
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
    protected boolean isBankHoliday(final DateTime givenDate) {
        final String bankHolidaySummary =
                this.bankHolidayService.getBankHolidaySummary(givenDate);
        return bankHolidaySummary != null;
    }
}

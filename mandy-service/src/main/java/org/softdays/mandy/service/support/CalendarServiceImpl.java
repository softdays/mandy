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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

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
    public DataGridDto getDataGridOfTheMonth(final LocalDate date) {
        LocalDate currentDate = this.getFirstMondayOfMonth(date);
        final LocalDate end = this.getFirstSundayAfterEndOfMonth(date);
        final DataGridDto dataGrid = new DataGridDto(date);
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

    private boolean isMonday(final LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.MONDAY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstDayOfTheMonth(java
     * .lang.String, java.lang.String)
     */
    @Override
    public LocalDate getFirstDayOfTheMonth(final String year, final String month) {

        return LocalDate.parse(year + month + FIRST_DAY_OF_MONTH, FORMATTER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.CalendarService#getFirstMondayOfMonth(java
     * .util.Date)
     */
    @Override
    public LocalDate getFirstMondayOfMonth(final LocalDate givenDate) {
        LocalDate date = YearMonth.from(givenDate).atDay(1);
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
    public LocalDate getFirstSundayAfterEndOfMonth(final LocalDate givenDate) {
        LocalDate date = YearMonth.from(givenDate).atEndOfMonth();
        while (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
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
    protected Status getDateStatus(final LocalDate givenDate) {
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
    protected boolean isSchoolHoliday(final LocalDate givenDate) {
        return this.schoolHolidaysService.isSchoolHoliday(givenDate);
    }

    /**
     * Checks if is end of week.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is end of week
     */
    protected boolean isEndOfWeek(final LocalDate date) {
        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY;
    }

    /**
     * Checks if is bank holiday.
     * 
     * @param givenDate
     *            the given date
     * @return true, if is bank holiday
     */
    protected boolean isBankHoliday(final LocalDate givenDate) {
        final String bankHolidaySummary = this.bankHolidayService.getBankHolidaySummary(givenDate);
        return bankHolidaySummary != null;
    }
}

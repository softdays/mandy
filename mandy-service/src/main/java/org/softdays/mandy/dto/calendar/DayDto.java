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
package org.softdays.mandy.dto.calendar;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * The Class DayDto.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class DayDto {

    /**
     * The Enum Status.
     * 
     * @author rpatriarche
     * @since 1.0.0
     */
    public enum Status {
        /**
         * Working day.
         */
        WD,
        /**
         * Bank Holiday.
         */
        BH,
        /**
         * Week End.
         */
        WE,
        /**
         * School Holiday (could be a working day).
         */
        SH
    }

    private LocalDate date;

    private Status status;

    @JsonBackReference
    private WeekDto week;

    /**
     * Instantiates a new day dto.
     */
    public DayDto() {
        super();
    }

    /**
     * Instantiates a new day dto.
     * 
     * @param week
     *            the week
     * @param date
     *            the date
     * @param status
     *            the status
     */
    public DayDto(final WeekDto week, final LocalDate date, final Status status) {
        super();
        this.date = date;
        this.week = week;
        this.status = status;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            the new date
     */
    public void setDate(final LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the status.
     * 
     * @return the status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * 
     * @param status
     *            the new status
     */
    public void setStatus(final Status status) {
        this.status = status;
    }

    /**
     * Gets the week.
     * 
     * @return the week
     */
    public WeekDto getWeek() {
        return this.week;
    }

    /**
     * Sets the week.
     * 
     * @param week
     *            the new week
     */
    public void setWeek(final WeekDto week) {
        this.week = week;
    }

}

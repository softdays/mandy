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
package org.softdays.mandy.dto.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.joda.time.DateTime;

/**
 * Contains the working weeks of the month to which belongs the given date.
 * 
 * @author rpatriarche
 * 
 */
public class DataGridDto {

    private String year;

    private String month;

    @JsonManagedReference
    private List<WeekDto> weeks = new ArrayList<>();

    /**
     * Instantiates a new data grid dto.
     */
    public DataGridDto() {
	super();
	weeks = new ArrayList<WeekDto>();
    }

    /**
     * Instantiates a new data grid dto.
     * 
     * @param base
     *            the base
     */
    public DataGridDto(Date base) {
	DateTime date = new DateTime(base);
	this.year = date.toString("yyyy");
	this.month = date.toString("MM");
    }

    /**
     * Gets the year.
     * 
     * @return the year
     */
    public String getYear() {
	return year;
    }

    /**
     * Sets the year.
     * 
     * @param year
     *            the new year
     */
    public void setYear(String year) {
	this.year = year;
    }

    /**
     * Gets the month.
     * 
     * @return the month
     */
    public String getMonth() {
	return month;
    }

    /**
     * Sets the month.
     * 
     * @param month
     *            the new month
     */
    public void setMonth(String month) {
	this.month = month;
    }

    /**
     * Gets the weeks.
     * 
     * @return the weeks
     */
    public List<WeekDto> getWeeks() {
	return Collections.unmodifiableList(weeks);
    }

    /**
     * New week.
     * 
     * @return the week dto
     */
    public WeekDto newWeek() {
	WeekDto weekDto = new WeekDto(this);
	this.weeks.add(weekDto);

	return weekDto;
    }

}

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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.joda.time.DateTime;
import org.softdays.mandy.dto.calendar.DayDto.Status;

/**
 * The Class WeekDto.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@JsonPropertyOrder({ "num", "days" })
public class WeekDto {

    public static final int WORKING_DAYS_IN_A_WEEK = 5;

    @JsonBackReference
    private DataGridDto parentGrid;

    @JsonManagedReference
    private List<DayDto> days;

    /**
     * Instantiates a new week dto.
     */
    public WeekDto() {
	super();
	days = new ArrayList<DayDto>();
    }

    /**
     * Instantiates a new week dto.
     * 
     * @param parentGrid
     *            the parent grid
     */
    public WeekDto(DataGridDto parentGrid) {
	this();
	this.parentGrid = parentGrid;
    }

    /**
     * Gets the num.
     * 
     * @return the num
     */
    public int getNum() {
	// return parentGrid.getWeeks().indexOf(this) + 1;
	return new DateTime(days.get(0).getDate()).getWeekOfWeekyear();
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(days.get(0).getDate());
	// return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Gets the days.
     * 
     * @return the days
     */
    public List<DayDto> getDays() {
	return Collections.unmodifiableList(days);
    }

    /**
     * Gets the parent grid.
     * 
     * @return the parent grid
     */
    public DataGridDto getParentGrid() {
	return parentGrid;
    }

    /**
     * Sets the parent grid.
     * 
     * @param parentGrid
     *            the new parent grid
     */
    public void setParentGrid(DataGridDto parentGrid) {
	this.parentGrid = parentGrid;
    }

    /**
     * New day.
     * 
     * @param date
     *            the date
     * @param status
     *            the status
     * @return the day dto
     */
    public DayDto newDay(Date date, Status status) {
	if (isCompleted()) {
	    throw new IllegalStateException("This week is already completed.");
	}
	DayDto day = new DayDto(this, date, status);
	this.days.add(day);

	return day;
    }

    /**
     * Checks if is completed.
     * 
     * @return true, if is completed
     */
    @JsonIgnore
    public boolean isCompleted() {
	return this.days.size() == WORKING_DAYS_IN_A_WEEK;
    }

}

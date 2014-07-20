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

@JsonPropertyOrder({ "num", "days" })
public class WeekDto {

    public static final int WORKING_DAYS_IN_A_WEEK = 5;

    @JsonBackReference
    private DataGridDto parentGrid;

    @JsonManagedReference
    private List<DayDto> days;

    public WeekDto() {
	super();
	days = new ArrayList<DayDto>();
    }

    public WeekDto(DataGridDto parentGrid) {
	this();
	this.parentGrid = parentGrid;
    }

    public int getNum() {
	// return parentGrid.getWeeks().indexOf(this) + 1;
	return new DateTime(days.get(0).getDate()).getWeekOfWeekyear();
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(days.get(0).getDate());
	// return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public List<DayDto> getDays() {
	return Collections.unmodifiableList(days);
    }

    public DataGridDto getParentGrid() {
	return parentGrid;
    }

    public void setParentGrid(DataGridDto parentGrid) {
	this.parentGrid = parentGrid;
    }

    public DayDto newDay(Date date, Status status) {
	if (isCompleted()) {
	    throw new IllegalStateException("This week is already completed.");
	}
	DayDto day = new DayDto(this, date, status);
	this.days.add(day);

	return day;
    }

    @JsonIgnore
    public boolean isCompleted() {
	return this.days.size() == WORKING_DAYS_IN_A_WEEK;
    }

}

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

    public DataGridDto() {
	super();
	weeks = new ArrayList<WeekDto>();
    }

    public DataGridDto(Date base) {
	DateTime date = new DateTime(base);
	this.year = date.toString("yyyy");
	this.month = date.toString("MM");
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public String getMonth() {
	return month;
    }

    public void setMonth(String month) {
	this.month = month;
    }

    public List<WeekDto> getWeeks() {
	return Collections.unmodifiableList(weeks);
    }

    public WeekDto newWeek() {
	WeekDto weekDto = new WeekDto(this);
	this.weeks.add(weekDto);

	return weekDto;
    }

}

package org.softdays.mandy.dto.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.softdays.mandy.web.serializer.JsonDateSerializer;

/**
 * Contains the working weeks of the month to which belongs the given date.
 * 
 * @author rpatriarche
 * 
 */
public class DataGridDto {

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date dateRef;

    @JsonManagedReference
    private List<WeekDto> weeks;

    public DataGridDto() {
	super();
	weeks = new ArrayList<WeekDto>();
    }

    public DataGridDto(final Date givenDate) {
	this();
	this.dateRef = new DateTime(givenDate).toDate();
    }

    public Date getDateRef() {
	return dateRef;
    }

    public void setDateRef(Date dateRef) {
	this.dateRef = dateRef;
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

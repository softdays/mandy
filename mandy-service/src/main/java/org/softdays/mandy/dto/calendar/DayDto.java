package org.softdays.mandy.dto.calendar;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.softdays.mandy.web.serializer.JsonDateSerializer;

public class DayDto {

    public enum Status {
	/**
	 * Working day
	 */
	WD,
	/**
	 * Bank Holiday
	 */
	BH,
	/**
	 * Week End
	 */
	WE,
	/**
	 * School Holiday (could be a working day)
	 */
	SH
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;

    private Status status;

    @JsonBackReference
    private WeekDto week;

    public DayDto() {
	super();
    }

    public DayDto(WeekDto week, Date date, Status status) {
	super();
	this.date = new DateTime(date).toDate();
	this.week = week;
	this.status = status;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = new DateTime(date).toDate();
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public WeekDto getWeek() {
	return week;
    }

    public void setWeek(WeekDto week) {
	this.week = week;
    }

}

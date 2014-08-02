package org.softdays.mandy.service.support;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.dto.calendar.DayDto.Status;
import org.softdays.mandy.dto.calendar.WeekDto;
import org.softdays.mandy.service.BankHolidayService;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.SchoolHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private BankHolidayService bankHolidayService;

    @Autowired
    private SchoolHolidayService schoolHolidaysService;

    @Override
    public DataGridDto getDataGridOfTheMonth(Date givenDate) {
	Date start = getFirstMondayBeforeStartOfMonth(givenDate);
	Date end = getFirstSundayAfterEndOfMonth(givenDate);
	Date currentDate = start;
	DataGridDto dataGrid = new DataGridDto(givenDate);
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

    @Override
    public Date getFirstMondayBeforeStartOfMonth(final Date givenDate) {
	DateTime date = new DateTime(givenDate);
	date = date.dayOfMonth().withMinimumValue();
	while (date.getDayOfWeek() != DateTimeConstants.MONDAY) {
	    date = date.minusDays(1);
	}

	return date.toDate();
    }

    @Override
    public Date getFirstSundayAfterEndOfMonth(final Date givenDate) {
	DateTime date = new DateTime(givenDate);
	date = date.dayOfMonth().withMaximumValue();
	while (date.getDayOfWeek() != DateTimeConstants.SUNDAY) {
	    date = date.plusDays(1);
	}

	return date.toDate();
    }

    protected Date next(Date date) {
	return new DateTime(date).plusDays(1).toDate();
    }

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

    protected boolean isSchoolHoliday(Date givenDate) {
	return schoolHolidaysService.isSchoolHoliday(givenDate);
    }

    protected boolean isEndOfWeek(Date givenDate) {
	DateTime date = new DateTime(givenDate);
	int dayOfWeek = date.getDayOfWeek();
	return dayOfWeek == DateTimeConstants.SUNDAY
		|| dayOfWeek == DateTimeConstants.SATURDAY;
    }

    protected boolean isBankHoliday(Date givenDate) {
	String bankHolidaySummary = bankHolidayService
		.getBankHolidaySummary(givenDate);
	return bankHolidaySummary != null;
    }
}
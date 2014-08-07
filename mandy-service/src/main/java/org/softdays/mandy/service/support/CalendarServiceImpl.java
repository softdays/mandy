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
    public DataGridDto getDataGridOfTheMonth(Date base) {
	// Date base = this.determineBaseDate(givenDate);
	Date start = getFirstMondayOfMonth(base);
	Date end = getFirstSundayAfterEndOfMonth(base);
	Date currentDate = start;
	DataGridDto dataGrid = new DataGridDto(base);
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

    /**
     * Ici le coeur de l'algo de construction dela grille. Il s'agit de
     * retourner le premier jour du mois correspondant à la grille à laquelle
     * appartient la date donnée. Attention, la date donnée peut être une date
     * du mois calendaire postérieur à celui de la grille de saisie à laquelle
     * elle appartient. Le fait de proposer une saisie débutant systématiquement
     * sur le premier lundi du mois courant fait qu'en début de mois, on peut
     * être amené à présenter la grille correspondant au mois calendaire
     * précédent.
     */
    protected Date determineBaseDate(Date givenDate) {
	DateTime base = new DateTime(givenDate);
	if (dateWeekContainsOneDayOfPreviousMonth(base)) {
	    base = base.minusMonths(1);
	}

	return base.dayOfMonth().withMinimumValue().toDate();
    }

    /**
     * Est-ce que la semaine du jour donné contient un jour du mois précédent le
     * mois de la date donnée ?
     * 
     * Does the week to which belongs the given date contain one day of the the
     * month which precedes the month of the given date?
     */
    protected boolean dateWeekContainsOneDayOfPreviousMonth(DateTime date) {
	int week = date.getWeekOfWeekyear();
	int month = date.getMonthOfYear();
	DateTime prev = date.minusDays(1);
	boolean sameMonth = true;
	while (prev.getWeekOfWeekyear() == week && sameMonth) {
	    sameMonth = prev.getMonthOfYear() == month;
	    prev = prev.minusDays(1);
	}
	return !sameMonth;
    }

    private boolean isMonday(DateTime date) {
	return date.getDayOfWeek() == DateTimeConstants.MONDAY;
    }

    @Override
    public Date getFirstMondayOfMonth(final Date givenDate) {
	DateTime date = new DateTime(givenDate);
	date = date.dayOfMonth().withMinimumValue();
	while (!isMonday(date)) {
	    date = date.plusDays(1);
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
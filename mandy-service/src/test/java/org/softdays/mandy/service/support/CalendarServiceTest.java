package org.softdays.mandy.service.support;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class CalendarServiceTest {

    private CalendarServiceImpl calendarService = new CalendarServiceImpl();
    private Date givenDate = new DateTime(2014, 7, 12, 0, 0).toDate();

    @Test
    public void getFirstMondayOfTheMonthCasNominal() {
	Date firstMonday = calendarService.getFirstMondayBeforeStartOfMonth(givenDate);
	Date expected = new DateTime(2014, 7, 7, 0, 0).toDate();
	Assert.assertEquals(expected, firstMonday);
    }

    @Test
    public void getFirstMondayOfTheMonthLimiteInf() {
	Date firstMonday = calendarService.getFirstMondayBeforeStartOfMonth(givenDate);
	Date expected = new DateTime(2014, 7, 7, 0, 0).toDate();
	Assert.assertEquals(expected, firstMonday);
    }

    @Test
    public void getFirstMondayOfTheMonthLimiteSup() {
	Date firstMonday = calendarService.getFirstMondayBeforeStartOfMonth(givenDate);
	Date expected = new DateTime(2014, 7, 7, 0, 0).toDate();
	Assert.assertEquals(expected, firstMonday);
    }

    @Test
    public void getFirstMondayOfTheMonthGivenDateEqualsFirstMonday() {
	Date givenDate = new DateTime(2014, 7, 7, 0, 0).toDate();
	Date firstMonday = calendarService.getFirstMondayBeforeStartOfMonth(givenDate);
	Assert.assertEquals(givenDate, firstMonday);
    }

    @Test
    public void getFirstSundayAfterEndOfMonthCasNominal() {
	Date lastSunday = calendarService
		.getFirstSundayAfterEndOfMonth(givenDate);
	Date expected = new DateTime(2014, 8, 3, 0, 0).toDate();
	Assert.assertEquals(expected, lastSunday);
    }

    @Test
    public void getFirstSundayAfterEndOfMonthLimiteSup() {
	Date givenDate = new DateTime(2014, 7, 31, 0, 0).toDate();
	Date lastSunday = calendarService
		.getFirstSundayAfterEndOfMonth(givenDate);
	Date expected = new DateTime(2014, 8, 3, 0, 0).toDate();
	Assert.assertEquals(expected, lastSunday);
    }

    @Test
    public void getFirstSundayAfterEndOfMonthLimiteInf() {
	Date givenDate = new DateTime(2014, 8, 1, 0, 0).toDate();
	Date lastSunday = calendarService
		.getFirstSundayAfterEndOfMonth(givenDate);
	Date expected = new DateTime(2014, 8, 31, 0, 0).toDate();
	Assert.assertEquals(expected, lastSunday);
    }

    @Test
    public void nextCasNominal() {
	Date next = calendarService.next(givenDate);
	Date expected = new DateTime(2014, 7, 13, 0, 0).toDate();
	Assert.assertEquals(expected, next);
    }

    @Test
    public void nextAvecChangementMoisAnnee() {
	Date givenDate = new DateTime(2014, 12, 31, 0, 0).toDate();
	Date next = calendarService.next(givenDate);
	Date expected = new DateTime(2015, 1, 1, 0, 0).toDate();
	Assert.assertEquals(expected, next);
    }

    @Test
    public void isEndOfWeekShouldBeTrue() {
	Date givenDate = new DateTime(2014, 7, 6, 0, 0).toDate();
	Assert.assertTrue(calendarService.isEndOfWeek(givenDate));
    }

    @Test
    public void isEndOfWeekShouldBeTrueToo() {
	Assert.assertTrue(calendarService.isEndOfWeek(givenDate));
    }

    @Test
    public void isEndOfWeekShouldBeFalse() {
	Date givenDate = new DateTime(2014, 7, 14, 0, 0).toDate();
	Assert.assertFalse(calendarService.isEndOfWeek(givenDate));
    }

    @Test
    public void testJodaTimeWeekNumberOfYear() {
	// ISO8601 week algorithm, the first week of the year
	// is that in which at least 4 days are in the year

	// en 2016 le 1/1 tombe un vendredi => numero de semaine : 53
	int nb = new DateTime(2016, 1, 1, 0, 0).getWeekOfWeekyear();
	Assert.assertEquals(53, nb);
    }

}

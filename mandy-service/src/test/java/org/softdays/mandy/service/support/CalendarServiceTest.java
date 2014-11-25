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
package org.softdays.mandy.service.support;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class CalendarServiceTest {

    private final CalendarServiceImpl calendarService = new CalendarServiceImpl();
    private final DateTime givenDate = new DateTime(2014, 7, 12, 0, 0);

    @Test
    public void getFirstMondayOfTheMonthCasNominal() {
        final DateTime firstMonday = this.calendarService
                .getFirstMondayOfMonth(this.givenDate);
        final DateTime expected = new DateTime(2014, 7, 7, 0, 0);
        Assert.assertEquals(expected, firstMonday);
    }

    @Test
    public void getFirstMondayOfTheMonthLimiteInf() {
        final DateTime firstMonday = this.calendarService
                .getFirstMondayOfMonth(this.givenDate);
        final DateTime expected = new DateTime(2014, 7, 7, 0, 0);
        Assert.assertEquals(expected, firstMonday);
    }

    @Test
    public void getFirstMondayOfTheMonthLimiteSup() {
        final DateTime firstMonday = this.calendarService
                .getFirstMondayOfMonth(this.givenDate);
        final DateTime expected = new DateTime(2014, 7, 7, 0, 0);
        Assert.assertEquals(expected, firstMonday);
    }

    @Test
    public void getFirstMondayOfTheMonthGivenDateEqualsFirstMonday() {
        final DateTime givenDate = new DateTime(2014, 7, 7, 0, 0);
        final DateTime firstMonday = this.calendarService
                .getFirstMondayOfMonth(givenDate);
        Assert.assertEquals(givenDate, firstMonday);
    }

    @Test
    public void getFirstSundayAfterEndOfMonthCasNominal() {
        final DateTime lastSunday = this.calendarService
                .getFirstSundayAfterEndOfMonth(this.givenDate);
        final DateTime expected = new DateTime(2014, 8, 3, 0, 0);
        Assert.assertEquals(expected, lastSunday);
    }

    @Test
    public void getFirstSundayAfterEndOfMonthLimiteSup() {
        final DateTime givenDate = new DateTime(2014, 7, 31, 0, 0);
        final DateTime lastSunday = this.calendarService
                .getFirstSundayAfterEndOfMonth(givenDate);
        final DateTime expected = new DateTime(2014, 8, 3, 0, 0);
        Assert.assertEquals(expected, lastSunday);
    }

    @Test
    public void getFirstSundayAfterEndOfMonthLimiteInf() {
        final DateTime givenDate = new DateTime(2014, 8, 1, 0, 0);
        final DateTime lastSunday = this.calendarService
                .getFirstSundayAfterEndOfMonth(givenDate);
        final DateTime expected = new DateTime(2014, 8, 31, 0, 0);
        Assert.assertEquals(expected, lastSunday);
    }

    @Test
    public void isEndOfWeekShouldBeTrue() {
        final DateTime givenDate = new DateTime(2014, 7, 6, 0, 0);
        Assert.assertTrue(this.calendarService.isEndOfWeek(givenDate));
    }

    @Test
    public void isEndOfWeekShouldBeTrueToo() {
        Assert.assertTrue(this.calendarService.isEndOfWeek(this.givenDate));
    }

    @Test
    public void isEndOfWeekShouldBeFalse() {
        final DateTime givenDate = new DateTime(2014, 7, 14, 0, 0);
        Assert.assertFalse(this.calendarService.isEndOfWeek(givenDate));
    }

    @Test
    public void testJodaTimeWeekNumberOfYear() {
        // ISO8601 week algorithm, the first week of the year
        // is that in which at least 4 days are in the year

        // en 2016 le 1/1 tombe un vendredi => numero de semaine : 53
        final int nb = new DateTime(2016, 1, 1, 0, 0).getWeekOfWeekyear();
        Assert.assertEquals(53, nb);
    }

}

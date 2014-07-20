package org.softdays.mandy.service.support;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.softdays.mandy.service.SchoolHolidayService;

public class SchoolHolidaysServiceTest {

    private SchoolHolidayService schoolHolidaysService = new SchoolHolidayServiceImpl();

    @Test
    public void shouldReturnsTrue() {
	Assert.assertTrue(schoolHolidaysService.isSchoolHoliday(new DateTime(
		2014, 7, 18, 0, 0).toDate()));
    }

    @Test
    public void shouldReturnsFalse() {
	Assert.assertFalse(schoolHolidaysService.isSchoolHoliday(new DateTime(
		2014, 9, 11, 0, 0).toDate()));
    }

    @Test
    public void shouldReturnsFalseAtLowerBoundary() {
	Assert.assertFalse(schoolHolidaysService.isSchoolHoliday(new DateTime(
		2014, 10, 18, 0, 0).toDate()));
    }

    @Test
    public void shouldReturnsFalseAtUpperBoundary() {
	Assert.assertFalse(schoolHolidaysService.isSchoolHoliday(new DateTime(
		2014, 11, 3, 0, 0).toDate()));
    }

    // @Test
    // public void shouldReturnsTrueAtInnerLowerBoundary() {
    // Assert.assertFalse(schoolHolidaysService.isSchoolHoliday(new DateTime(
    // 2014, 10, 19, 0, 0).toDate()));
    // }
    //
    // @Test
    // public void shouldReturnsTrueAtInnerUpperBoundary() {
    // Assert.assertFalse(schoolHolidaysService.isSchoolHoliday(new DateTime(
    // 2014, 11, 2, 0, 0).toDate()));
    // }
}

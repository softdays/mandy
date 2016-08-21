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

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.softdays.mandy.service.SchoolHolidayService;

public class SchoolHolidaysServiceTest {

    private final SchoolHolidayService schoolHolidaysService = new SchoolHolidayServiceImpl();

    @Test
    public void shouldReturnsTrue() {
        Assert.assertTrue(this.schoolHolidaysService.isSchoolHoliday(LocalDate.of(2014, 7, 18)));
    }

    @Test
    public void shouldReturnsFalse() {
        Assert.assertFalse(this.schoolHolidaysService.isSchoolHoliday(LocalDate.of(2014, 9, 11)));
    }

    @Test
    public void shouldReturnsFalseAtLowerBoundary() {
        Assert.assertFalse(this.schoolHolidaysService.isSchoolHoliday(LocalDate.of(2014, 10, 18)));
    }

    @Test
    public void shouldReturnsFalseAtUpperBoundary() {
        Assert.assertFalse(this.schoolHolidaysService.isSchoolHoliday(LocalDate.of(2014, 11, 3)));
    }

}

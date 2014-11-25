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

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softdays.mandy.config.SpringConfiguration;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.dto.calendar.WeekDto;
import org.softdays.mandy.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class CalendarServiceDataGrid7thJul2014Test {

    @Autowired
    private CalendarService calendarService;

    private final DateTime givenDate = new DateTime(2014, 7, 19, 0, 0);

    private DataGridDto grid;

    @PostConstruct
    public void init() {
        this.grid = this.calendarService.getDataGridOfTheMonth(this.givenDate);
    }

    @Test
    public void theYearRefShouldBeSaved() {
        // sauvegarde de l'année de référence
        Assert.assertEquals("2014", this.grid.getYear());
    }

    @Test
    public void theMonthRefShouldBeSaved() {
        // sauvegarde de l'année de référence
        Assert.assertEquals("07", this.grid.getMonth());
    }

    @Test
    public void datagridShouldContainsFourWeeks() {
        Assert.assertEquals(4, this.grid.getWeeks().size());
    }

    @Test
    public void eachWeekShouldContainsFiveDays() {
        for (final WeekDto week : this.grid.getWeeks()) {
            Assert.assertEquals(5, week.getDays().size());
        }

    }

    @Test
    public void checkFirstDayOfTheDataGrid() {
        Assert.assertEquals(new DateTime(2014, 7, 7, 0, 0).toDate(), this.grid
                .getWeeks().get(0).getDays().get(0).getDate());
    }

    @Test
    public void checkLastDayOfTheDataGrid() {
        Assert.assertEquals(new DateTime(2014, 8, 1, 0, 0).toDate(), this.grid
                .getWeeks().get(3).getDays().get(4).getDate());
    }

}

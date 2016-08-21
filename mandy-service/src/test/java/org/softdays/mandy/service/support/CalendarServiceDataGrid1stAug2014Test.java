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

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.dto.calendar.WeekDto;
import org.softdays.mandy.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Ignore
// Ignore: on ne soucie plus des cas spécifiques concernant la date de référence
// car c'est toujours le premier jour du mois qui est passé en paramètre
public class CalendarServiceDataGrid1stAug2014Test {

    @Autowired
    private CalendarService calendarService;

    /**
     * Intérêt du test : le jour référence est le dernier jour de la grille du
     * mois précédent. On doit retourner la grille du mois de juillet car le
     * 01/08/2014 est le dernier jour de la dernière semaine de juillet 2014.
     */
    private final LocalDate givenDate = LocalDate.of(2014, 8, 1);

    private DataGridDto grid;

    @PostConstruct
    public void init() {
        this.grid = this.calendarService.getDataGridOfTheMonth(this.givenDate);
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
        Assert.assertEquals(LocalDate.of(2014, 7, 7),
                this.grid.getWeeks().get(0).getDays().get(0).getDate());
    }

    @Test
    public void checkLastDayOfTheDataGrid() {
        Assert.assertEquals(LocalDate.of(2014, 8, 1),
                this.grid.getWeeks().get(3).getDays().get(4).getDate());
    }

}

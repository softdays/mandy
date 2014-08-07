package org.softdays.mandy.service.support;

import java.util.Date;

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

    private Date givenDate = new DateTime(2014, 7, 19, 0, 0).toDate();

    private DataGridDto grid;

    @PostConstruct
    public void init() {
	grid = calendarService.getDataGridOfTheMonth(givenDate);
    }

    @Test
    public void theYearRefShouldBeSaved() {
	// sauvegarde de l'année de référence
	Assert.assertEquals("2014", grid.getYear());
    }

    @Test
    public void theMonthRefShouldBeSaved() {
	// sauvegarde de l'année de référence
	Assert.assertEquals("07", grid.getMonth());
    }

    @Test
    public void datagridShouldContainsFourWeeks() {
	Assert.assertEquals(4, grid.getWeeks().size());
    }

    @Test
    public void eachWeekShouldContainsFiveDays() {
	for (WeekDto week : grid.getWeeks()) {
	    Assert.assertEquals(5, week.getDays().size());
	}

    }

    @Test
    public void checkFirstDayOfTheDataGrid() {
	Assert.assertEquals(new DateTime(2014, 7, 7, 0, 0).toDate(), grid
		.getWeeks().get(0).getDays().get(0).getDate());
    }

    @Test
    public void checkLastDayOfTheDataGrid() {
	Assert.assertEquals(new DateTime(2014, 8, 1, 0, 0).toDate(), grid
		.getWeeks().get(3).getDays().get(4).getDate());
    }

}

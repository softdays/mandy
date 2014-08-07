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
public class CalendarServiceDataGrid1stSep2014Test {

    @Autowired
    private CalendarService calendarService;

    /**
     * Intérêt du test : cette grille contient 5 semaines
     */
    private Date givenDate = new DateTime(2014, 9, 1, 0, 0).toDate();

    private DataGridDto grid;

    @PostConstruct
    public void init() {
	grid = calendarService.getDataGridOfTheMonth(givenDate);
    }

    @Test
    public void datagridShouldContainsFiveWeeks() {
	Assert.assertEquals(5, grid.getWeeks().size());
    }

    @Test
    public void eachWeekShouldContainsFiveDays() {
	for (WeekDto week : grid.getWeeks()) {
	    Assert.assertEquals(5, week.getDays().size());
	}
    }

    @Test
    public void checkFirstDayOfTheDataGrid() {
	Assert.assertEquals(new DateTime(2014, 9, 1, 0, 0).toDate(), grid
		.getWeeks().get(0).getDays().get(0).getDate());
    }

    @Test
    public void checkLastDayOfTheDataGrid() {
	Assert.assertEquals(new DateTime(2014, 10, 3, 0, 0).toDate(), grid
		.getWeeks().get(4).getDays().get(4).getDate());
    }

}

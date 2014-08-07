package org.softdays.mandy.service.support;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
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
    private Date givenDate = new DateTime(2014, 8, 1, 0, 0).toDate();

    private DataGridDto grid;

    @PostConstruct
    public void init() {
	grid = calendarService.getDataGridOfTheMonth(givenDate);
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

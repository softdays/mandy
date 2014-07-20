package org.softdays.mandy.service.support;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softdays.mandy.config.SpringConfiguration;
import org.softdays.mandy.service.BankHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class BankHolidayServiceTest {

    @Autowired
    private BankHolidayService bankHolidayService;

    @Test
    public void isBankHolidaySouldReturnTrue() {
	Date givenDate = new DateTime(2014, 7, 14, 0, 0).toDate();
	String desc = bankHolidayService.getBankHolidaySummary(givenDate);
	Assert.assertEquals("Bastille Day", desc);
    }
}

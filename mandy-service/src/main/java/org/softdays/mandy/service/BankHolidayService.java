package org.softdays.mandy.service;

import java.util.Date;

public interface BankHolidayService {

    /**
     * Returns an US summary describing the bank holiday if the given date is a
     * bank holiday, NULL if the given date is not a bank holiday.
     */
    String getBankHolidaySummary(Date givenDate);

}

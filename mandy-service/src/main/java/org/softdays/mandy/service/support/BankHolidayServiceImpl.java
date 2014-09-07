/**
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softdays.mandy.service.BankHolidayService;
import org.springframework.stereotype.Service;

/**
 * The Class BankHolidayServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
public class BankHolidayServiceImpl implements BankHolidayService {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(BankHolidayServiceImpl.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormat
	    .forPattern("yyyyMMdd");

    private Map<Date, String> bankHolidays = new HashMap<>();

    /**
     * Instantiates a new bank holiday service impl.
     */
    public BankHolidayServiceImpl() {
	super();
	init();
    }

    private void init() {
	long start = System.currentTimeMillis();
	InputStream ics = this.getClass().getClassLoader()
		.getResourceAsStream("holidays/basic.ics");
	CalendarBuilder builder = new CalendarBuilder();
	Calendar calendar = null;
	try {
	    calendar = builder.build(ics);
	} catch (IOException | ParserException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}

	ComponentList components = calendar.getComponents();
	@SuppressWarnings("rawtypes")
	Iterator i = components.iterator();
	while (i.hasNext()) {
	    Component component = (Component) i.next();
	    Property dstart = component.getProperty("DTSTART");
	    Property summary = component.getProperty("SUMMARY");
	    DateTime date = FORMATTER.parseDateTime(dstart.getValue());
	    bankHolidays.put(date.toDate(), summary.getValue());
	}
	long end = System.currentTimeMillis();
	if (LOGGER.isTraceEnabled()) {
	    LOGGER.trace("init duration: " + (end - start) + "ms");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.BankHolidayService#getBankHolidaySummary(java
     * .util.Date)
     */
    @Override
    public String getBankHolidaySummary(Date givenDate) {
	return bankHolidays.get(givenDate);
    }

}

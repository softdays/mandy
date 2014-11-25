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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.softdays.mandy.core.exception.UnrecoverableException;
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

    private final Map<DateTime, String> bankHolidays =
            new ConcurrentHashMap<>();

    /**
     * Instantiates a new bank holiday service impl.
     */
    public BankHolidayServiceImpl() {
        super();
        try {
            this.init();
        } catch (IOException | ParserException e) {
            throw new UnrecoverableException(e);
        }
    }

    /**
     * Initializes the bean.
     * 
     * @throws IOException
     *             thrown when unable to read from the specified reader
     * @throws ParserException
     *             thrown if an error occurs during parsing
     */
    private void init() throws IOException, ParserException {
        final long start = System.currentTimeMillis();
        final ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
        final InputStream ics =
                classLoader.getResourceAsStream("holidays/basic.ics");
        final CalendarBuilder builder = new CalendarBuilder();
        final Calendar calendar = builder.build(ics);

        final ComponentList components = calendar.getComponents();
        @SuppressWarnings("rawtypes")
        final Iterator iterator = components.iterator();
        while (iterator.hasNext()) {
            final Component component = (Component) iterator.next();
            final Property dstart = component.getProperty("DTSTART");
            final Property summary = component.getProperty("SUMMARY");
            final DateTime date = FORMATTER.parseDateTime(dstart.getValue());
            this.bankHolidays.put(date, summary.getValue());
        }
        if (LOGGER.isTraceEnabled()) {
            final long duration = System.currentTimeMillis() - start;
            LOGGER.trace(String.format("init duration: %s ms", duration));
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
    public String getBankHolidaySummary(final DateTime givenDate) {
        return this.bankHolidays.get(givenDate);
    }

}

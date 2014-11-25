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
package org.softdays.mandy.dto.calendar;

import java.util.Date;

/**
 * The Class BankHolidayDto.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class BankHolidayDto {

    private String label;

    private Date date;

    /**
     * Instantiates a new bank holiday dto.
     */
    public BankHolidayDto() {
        super();
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            the new label
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
        return (Date) this.date.clone();
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            the new date
     */
    public void setDate(final Date date) {
        this.date = (Date) date.clone();
    }

}

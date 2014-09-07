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
package org.softdays.mandy.model;

/**
 * The Enum Quota.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public enum Quota {
    NONE(0f), QUARTER(0.25f), HALF(0.5f), THREE_QUARTERS(0.75f), WHOLE(1f);

    private float value;

    private Quota(float value) {
	this.value = value;
    }

    /**
     * Float value.
     * 
     * @return the float
     */
    public Float floatValue() {
	return value;
    }

    /**
     * Value of.
     * 
     * @param value
     *            the value
     * @return the quota
     */
    public static Quota valueOf(float value) {
	Quota result = null;
	for (Quota quota : Quota.values()) {
	    if (quota.value == value) {
		result = quota;
		break;
	    }
	}
	return result;
    }
}

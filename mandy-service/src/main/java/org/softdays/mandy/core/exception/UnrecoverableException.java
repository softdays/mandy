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
package org.softdays.mandy.core.exception;

/**
 * If a client cannot do anything to recover from the exception, make it an
 * unchecked exception.
 * 
 * @see <a href=
 *      "https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html"
 *      >Oracle documentation: Unchecked Exceptions — The Controversy</a>
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class UnrecoverableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new mandy exception.
     * 
     * @param throwable
     *            the throwable to wrap.
     */
    public UnrecoverableException(final Throwable throwable) {
        super(throwable);
    }

}

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
package org.softdays.mandy.web.resources;

import javax.ws.rs.core.MediaType;

/**
 * Provides Mandy constants.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public final class MandyConstants {

    public static final String ENCODING = "utf-8";
    public static final String CHARSET = ";charset=" + ENCODING;
    public static final String JSON_UTF8 = MediaType.APPLICATION_JSON + CHARSET;

    public static final String URI_ROOT = "/";
    public static final String URI_ACTIVITIES = "activities";
    public static final String URI_RESOURCES = "resources";
    public static final String URI_DATAGRID_GET = "datagrid/{year}/{month}";
    public static final String URI_IMPUTATIONS = "imputations";
    public static final String URI_IMPUTATIONS_GET = URI_IMPUTATIONS
            + "/{year}/{month}";

    public static final String URI_IMPUTATIONS_ID = URI_IMPUTATIONS + "/{id}";

    private MandyConstants() {
        super();
    }

}

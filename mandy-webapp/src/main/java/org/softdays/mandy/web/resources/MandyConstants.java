package org.softdays.mandy.web.resources;

import javax.ws.rs.core.MediaType;

public class MandyConstants {

    public static final String ENCODING = "utf-8";
    public static final String CHARSET = ";charset=" + ENCODING;
    public static final String JSON_UTF8 = MediaType.APPLICATION_JSON + CHARSET;

    public static final String URI_ROOT = "/";
    public static final String URI_ACTIVITIES = "activities";
    public static final String URI_CALENDAR_DATAGRID = "datagrid/{year}/{month}";

    private MandyConstants() {
	super();
    }

}

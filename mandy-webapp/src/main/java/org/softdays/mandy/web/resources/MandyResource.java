package org.softdays.mandy.web.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.service.ActivityService;
import org.softdays.mandy.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * Created by rpatriarche on 09/03/14.
 */
@Component
@Path(MandyConstants.URI_ROOT)
public class MandyResource {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CalendarService calendarService;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
	    "yyyy-MM-dd");

    @GET
    @Path(MandyConstants.URI_ACTIVITIES)
    @Produces(MandyConstants.JSON_UTF8)
    public List<ActivityDto> activities() {
	return activityService.getActivities();
    }

    @GET
    @Path(MandyConstants.URI_CALENDAR_DATAGRID)
    @Produces(MandyConstants.JSON_UTF8)
    public DataGridDto caldendar(@PathParam("date") String strDate) {
	Date date = null;
	try {
	    date = DATE_FORMATTER.parse(strDate);
	} catch (ParseException e) {
	    throw new WebApplicationException(Response
		    .status(Status.BAD_REQUEST)
		    .entity("Couldn't parse date string: " + e.getMessage())
		    .build());
	}
	return calendarService.getDataGridOfTheMonth(date);
    }
}

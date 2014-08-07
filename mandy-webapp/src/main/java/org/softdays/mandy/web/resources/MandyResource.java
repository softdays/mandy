package org.softdays.mandy.web.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.softdays.mandy.config.Configuration;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.dto.CredentialsDto;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.service.ActivityService;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.web.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

    @Autowired
    private Configuration configurationDto;

    @Autowired
    private AuthService authService;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
	    "yyyyMMdd");

    @POST
    @Path("/login")
    @Consumes(MandyConstants.JSON_UTF8)
    public Response login(final CredentialsDto credentials) {
	ResponseBuilder httpResponse = null;
	if ("repatriarche".equalsIgnoreCase(credentials.getLogin())
		&& "repatriarche".equals(credentials.getPassword())) {
	    String location = "/" + configurationDto.getContextRoot()
		    + "/index.html?#/datagrid";
	    try {
		httpResponse = Response.temporaryRedirect(new URI(location));
	    } catch (URISyntaxException e) {
		e.printStackTrace();
		httpResponse = Response.serverError().entity(e.getMessage());
	    }
	} else {
	    httpResponse = Response.status(Response.Status.FORBIDDEN)
		    .entity("Utilisateur non reconnu.")
		    .type(MediaType.TEXT_PLAIN + MandyConstants.CHARSET);
	}

	return httpResponse.build();
    }

    @GET
    @Path("/user/session")
    @Produces(MandyConstants.JSON_UTF8)
    public ResourceDto currentUser() {

	return authService.getCurrentToken();
    }

    @GET
    @Path(MandyConstants.URI_ACTIVITIES)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public List<ActivityDto> activities() {
	ResourceDto user = authService.getCurrentToken();

	return activityService.getActivities(user.getResourceId());
    }

    @GET
    @Path(MandyConstants.URI_CALENDAR_DATAGRID)
    @Produces(MandyConstants.JSON_UTF8)
    public DataGridDto datagrid(@PathParam("year") String year,
	    @PathParam("month") String month) {
	Date date = null;
	try {
	    date = DATE_FORMATTER.parse(year + month + "01");
	} catch (ParseException e) {
	    throw new WebApplicationException(Response
		    .status(Status.BAD_REQUEST)
		    .entity("Couldn't parse date string: " + e.getMessage())
		    .build());
	}
	return calendarService.getDataGridOfTheMonth(date);
    }
}

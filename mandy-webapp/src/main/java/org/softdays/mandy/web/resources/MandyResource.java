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
package org.softdays.mandy.web.resources;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.softdays.mandy.config.Configuration;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.service.ActivityService;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.ImputationService;
import org.softdays.mandy.web.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by rpatriarche on 09/03/14.
 */
@Component
@Path(MandyConstants.URI_ROOT)
public class MandyResource {

    private static final Logger LOGGER = Logger.getLogger(MandyResource.class);

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ImputationService imputationService;

    @Autowired
    private Configuration configurationDto;

    @Autowired
    private AuthService authService;

    // @POST
    // @Path("/login")
    // @Consumes(MandyConstants.JSON_UTF8)
    // public Response login(final CredentialsDto credentials) {
    // ResponseBuilder httpResponse = null;
    // if ("repatriarche".equalsIgnoreCase(credentials.getLogin())
    // && "repatriarche".equals(credentials.getPassword())) {
    // String location = "/" + configurationDto.getContextRoot()
    // + "/index.html?#/datagrid";
    // try {
    // httpResponse = Response.temporaryRedirect(new URI(location));
    // } catch (URISyntaxException e) {
    // e.printStackTrace();
    // httpResponse = Response.serverError().entity(e.getMessage());
    // }
    // } else {
    // httpResponse = Response.status(Response.Status.FORBIDDEN)
    // .entity("Utilisateur non reconnu.")
    // .type(MediaType.TEXT_PLAIN + MandyConstants.CHARSET);
    // }
    //
    // return httpResponse.build();
    // }

    @GET
    @Path("/user/session")
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
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
    @Path(MandyConstants.URI_DATAGRID)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public DataGridDto datagrid(@PathParam("year") String year,
	    @PathParam("month") String month) {
	Date date;
	try {
	    date = calendarService.getFirstDayOfTheMonth(year, month);
	} catch (ParseException e) {
	    throw new WebApplicationException(Response
		    .status(Status.BAD_REQUEST)
		    .entity("Couldn't parse date string: " + e.getMessage())
		    .build());
	}

	return calendarService.getDataGridOfTheMonth(date);
    }

    @GET
    @Path(MandyConstants.URI_IMPUTATIONS_YEAR_MONTH)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public Map<Long, List<ImputationDto>> getImputations(
	    @PathParam("year") String year, @PathParam("month") String month) {
	Long resourceId = authService.getCurrentToken().getResourceId();
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("resourceId: " + resourceId);
	    LOGGER.debug("year: " + year);
	    LOGGER.debug("month: " + month);
	}
	Date date;
	try {
	    date = calendarService.getFirstDayOfTheMonth(year, month);
	} catch (ParseException e) {
	    throw new WebApplicationException(Response
		    .status(Status.BAD_REQUEST)
		    .entity("Couldn't parse date string: " + e.getMessage())
		    .build());
	}

	return imputationService.findImputations(resourceId, date);
    }

    @POST
    @Path(MandyConstants.URI_IMPUTATIONS)
    @Consumes(MandyConstants.JSON_UTF8)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public ImputationDto createImputation(ImputationDto newImputation) {
	return imputationService.createImputation(newImputation);
    }

    @PUT
    @Path(MandyConstants.URI_IMPUTATIONS_IID)
    @Consumes(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public void updateImputation(ImputationDto imputationDto) {
	String comment = imputationDto.getComment();
	if (StringUtils.hasText(comment)) {
	    if (comment.equals("booom")) {
		throw new RuntimeException("Booom");
	    }
	}
	imputationService.updateImputation(imputationDto);
    }

    @DELETE
    @Path(MandyConstants.URI_IMPUTATIONS_IID)
    @Consumes(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public void deleteImputation(@PathParam("id") Long imputationId) {
	imputationService.deleteImputation(imputationId);
    }

}

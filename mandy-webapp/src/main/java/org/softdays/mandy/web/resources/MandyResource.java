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
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.dto.PreferencesDto;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.dto.calendar.DataGridDto;
import org.softdays.mandy.service.ActivityService;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.ImputationService;
import org.softdays.mandy.service.ResourceService;
import org.softdays.mandy.web.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * Provides core services as REST API.
 * 
 * @author rpatriarche
 * @since 1.0.0
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
    private AuthService authService;

    @Autowired
    private ResourceService resourceService;

    /**
     * Instantiates a new mandy resource.
     */
    public MandyResource() {
        super();
    }

    /**
     * Current user.
     * 
     * @return the resource dto
     */
    @GET
    @Path("user")
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public ResourceDto currentUser() {

        return this.authService.getCurrentToken();
    }

    @GET
    @Path("preferences")
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public PreferencesDto getPreferences() {
        final Long currentUserId = this.authService.getCurrentUserId();
        return this.resourceService.findResourcePreferences(currentUserId);
    }

    @PUT
    @Path("preferences")
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public PreferencesDto updatePreferences(final PreferencesDto preferences) {
        final Long currentUserId = this.authService.getCurrentUserId();
        preferences.setResourceId(currentUserId); // just to be sure
        return this.resourceService.updateResourcePreferences(preferences);
    }

    /**
     * Activities.
     * 
     * @return the list
     */
    @GET
    @Path(MandyConstants.URI_ACTIVITIES)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public List<ActivityDto> getUserActivities(
            @QueryParam("filter") final String filter) {
        final Long currentUserId = this.authService.getCurrentUserId();

        return this.activityService.getActivities(currentUserId);
    }

    /**
     * Creates sub activity.
     * 
     * @param newImputation
     *            the new imputation
     * @return the imputation dto
     */
    @POST
    @Path(MandyConstants.URI_ACTIVITIES)
    @Consumes(MandyConstants.JSON_UTF8)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public ActivityDto createSubActivity(final ActivityDto activityDto) {
        return this.activityService.createSubActivity(activityDto);
    }

    /**
     * Datagrid.
     * 
     * @param year
     *            the year
     * @param month
     *            the month
     * @return the data grid dto
     */
    @GET
    @Path(MandyConstants.URI_DATAGRID_GET)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public DataGridDto datagrid(@PathParam("year") final String year,
            @PathParam("month") final String month) {
        final DateTime date = this.calendarService.getFirstDayOfTheMonth(year,
                month);

        return this.calendarService.getDataGridOfTheMonth(date);
    }

    /**
     * Gets the imputations.
     * 
     * @param year
     *            the year
     * @param month
     *            the month
     * @return the imputations
     */
    @GET
    @Path(MandyConstants.URI_IMPUTATIONS_GET)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public Map<Long, List<ImputationDto>> getImputations(
            @PathParam("year") final String year,
            @PathParam("month") final String month) {
        final Long resourceId = this.authService.getCurrentToken()
                .getResourceId();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("resourceId: " + resourceId);
            LOGGER.debug("year: " + year);
            LOGGER.debug("month: " + month);
        }
        final DateTime date = this.calendarService.getFirstDayOfTheMonth(year,
                month);

        return this.imputationService.findImputations(resourceId, date);
    }

    /**
     * Creates the imputation.
     * 
     * @param newImputation
     *            the new imputation
     * @return the imputation dto
     */
    @POST
    @Path(MandyConstants.URI_IMPUTATIONS)
    @Consumes(MandyConstants.JSON_UTF8)
    @Produces(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public ImputationDto createImputation(final ImputationDto newImputation) {
        return this.imputationService.createImputation(newImputation);
    }

    /**
     * Update imputation.
     * 
     * @param imputationDto
     *            the imputation dto
     * @return the imputation dto
     */
    @PUT
    @Path(MandyConstants.URI_IMPUTATIONS_ID)
    @Consumes(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public ImputationDto updateImputation(final ImputationDto imputationDto) {

        return this.imputationService.updateImputation(imputationDto);
    }

    /**
     * Delete imputation.
     * 
     * @param imputationId
     *            the imputation id
     */
    @DELETE
    @Path(MandyConstants.URI_IMPUTATIONS_ID)
    @Consumes(MandyConstants.JSON_UTF8)
    @Secured("ROLE_USER")
    public void deleteImputation(@PathParam("id") final Long imputationId) {
        this.imputationService.deleteImputation(imputationId);
    }
}

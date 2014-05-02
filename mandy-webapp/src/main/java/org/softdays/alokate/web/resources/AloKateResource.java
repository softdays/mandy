package org.softdays.alokate.web.resources;

import org.softdays.alokate.dto.ActivityDto;
import org.softdays.alokate.dto.TestDto;
import org.softdays.alokate.model.Activity;
import org.softdays.alokate.service.ActivityService;
import org.softdays.alokate.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rpatriarche on 09/03/14.
 */
@Component
@Path("/")
public class AloKateResource {

    @Resource
    private ActivityService activityService;

    @GET
    @Path("activities")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ActivityDto> activities() {
        return activityService.getActivities();
    }
}

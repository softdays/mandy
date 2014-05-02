
package org.softdays.alokate.web.resources;

import org.softdays.alokate.dto.TestDto;
import org.softdays.alokate.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Path("/test")
@Component
public class TestResource {

    @Autowired
    private TestService testService;

    /**
     * Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     *
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TestDto test() {
        return testService.findById(978);
    }
}

package org.softdays.mandy.web.config;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        this.packages("org.softdays.mandy.web.resources");
        // this.register(MandyResource.class);
        // this.register(ObjectMapperContextResolver.class);
        // this.register(JacksonFeature.class);
    }

}

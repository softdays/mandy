package org.softdays.mandy.web.resources;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Permet de configurer la serialisation JSON. Notamment pour les dates.
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    /**
     * Le mapper pouvant être récupéré à de nombreuses reprises, il faut factoriser sa création.
     */
    public ObjectMapperContextResolver() {
        super();
        this.mapper = new ObjectMapper();

        this.mapper.registerModule(new JavaTimeModule());

        // this.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // this.mapper.configure(SerializationFeature.<vWRITE_DATES_AS_TIMESTAMPS, false);

        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {

        return this.mapper;
    }

}

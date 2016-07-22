package org.softdays.mandy.dto.mapping;

import org.softdays.mandy.core.model.Resource;
import org.softdays.mandy.core.model.Resource.Role;
import org.softdays.mandy.dto.ResourceDto;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIoC = IoC.SPRING,
        withCustomFields = { @Field({ "Resource.id", "resourceId" }),
                @Field({ "Resource.uid", "login" }) },
        withIgnoreFields = { "imputations", "teams" })
public abstract class ResourceMapper {

    public abstract ResourceDto map(Resource source);

    public abstract Resource map(ResourceDto source);

    public String map(final Role source) {
        return source.name();
    }

    public Role map(final String source) {
        return Role.valueOf(source);
    }

}

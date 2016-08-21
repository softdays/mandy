package org.softdays.mandy.dto.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.softdays.mandy.core.model.Resource;
import org.softdays.mandy.dto.ResourceDto;

@Mapper
public interface ResourceMapper {

    @Mapping(source = "id", target = "resourceId")
    @Mapping(source = "uid", target = "login")
    ResourceDto map(Resource source);

    @InheritInverseConfiguration
    @Mapping(target = "imputations", ignore = true)
    @Mapping(target = "teams", ignore = true)
    Resource map(ResourceDto source);

}

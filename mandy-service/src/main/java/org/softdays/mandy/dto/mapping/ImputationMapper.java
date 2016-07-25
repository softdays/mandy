package org.softdays.mandy.dto.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.softdays.mandy.core.model.Imputation;
import org.softdays.mandy.dto.ImputationDto;

@Mapper
public interface ImputationMapper {

    @Mapping(source = "id", target = "imputationId")
    @Mapping(source = "activity.id", target = "activityId")
    @Mapping(source = "resource.id", target = "resourceId")
    ImputationDto map(Imputation source);

    @InheritInverseConfiguration
    Imputation map(ImputationDto source);
}

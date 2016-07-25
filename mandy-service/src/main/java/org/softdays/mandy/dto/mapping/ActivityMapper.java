package org.softdays.mandy.dto.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.softdays.mandy.core.model.Activity;
import org.softdays.mandy.dto.ActivityDto;

@Mapper
public interface ActivityMapper {

    // @Mapping(source = "category.pk", target = "category")
    // @Mapping(source = "type.pk", target = "type")
    // the code above works for this signature but not for the inverse signature because MapStruct
    // tries to instanciate enum
    @Mapping(target = "category", expression = "java( src.getCategory().getPk() )")
    @Mapping(target = "type", expression = "java( src.getType().getPk() )")
    @Mapping(source = "parentActivity.id", target = "parentActivityId")
    ActivityDto map(Activity src);

    @InheritInverseConfiguration
    @Mapping(target = "imputations", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "type",
            expression = "java( org.softdays.mandy.core.model.ActivityType.fromCode(src.getType()) )")
    @Mapping(target = "category",
            expression = "java( org.softdays.mandy.core.model.ActivityCategory.fromCode(src.getCategory()) )")
    Activity map(ActivityDto src);

}

package org.softdays.mandy.dto.mapping;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.softdays.mandy.core.model.Preference;
import org.softdays.mandy.dto.PreferencesDto;

@Mapper
@DecoratedWith(PreferenceMapperDecorator.class)
public interface PreferenceMapper {

    @Mapping(target = "activitiesFilter", ignore = true)
    @Mapping(source = "id", target = "resourceId")
    PreferencesDto map(Preference source);

    @Mapping(target = "filteredActivities", ignore = true)
    @Mapping(target = "resource", ignore = true)
    @InheritInverseConfiguration
    Preference map(PreferencesDto source);

}

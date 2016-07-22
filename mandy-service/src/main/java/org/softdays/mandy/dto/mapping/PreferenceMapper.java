package org.softdays.mandy.dto.mapping;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.softdays.mandy.core.model.Activity;
import org.softdays.mandy.core.model.Preference;
import org.softdays.mandy.dto.PreferencesDto;
import org.springframework.util.CollectionUtils;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING, withCustomFields = {
        @Field({ "id", "resourceId" }), @Field({ "filteredActivities", "activitiesFilter" }) })
public abstract class PreferenceMapper {

    public abstract PreferencesDto asDto(Preference source);

    public abstract Preference asEntity(PreferencesDto source);

    public List<Long> asIdList(final List<Activity> filteredActivities) {

        return convert(filteredActivities, Activity::getId);
    }

    public List<Activity> astActivityList(final List<Long> activitiesFilter) {

        return convert(activitiesFilter, id -> new Activity(id));
    }

    private static <I, O> List<O> convert(final List<I> in,
            final Function<? super I, ? extends O> predicate) {

        if (CollectionUtils.isEmpty(in)) {

            return Collections.emptyList();
        }

        return in.stream().map(predicate).collect(Collectors.toList());
    }

}

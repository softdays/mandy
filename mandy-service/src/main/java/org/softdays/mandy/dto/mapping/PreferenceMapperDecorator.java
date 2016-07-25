package org.softdays.mandy.dto.mapping;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.softdays.mandy.core.model.Activity;
import org.softdays.mandy.core.model.Preference;
import org.softdays.mandy.dto.PreferencesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

public abstract class PreferenceMapperDecorator implements PreferenceMapper {

    @Autowired
    @Qualifier("delegate")
    private PreferenceMapper delegate;

    @Override
    public PreferencesDto map(final Preference source) {
        PreferencesDto dto = this.delegate.map(source);
        dto.setActivitiesFilter(this.asIdList(source.getFilteredActivities()));

        return dto;
    }

    @Override
    public Preference map(final PreferencesDto source) {
        Preference entity = this.delegate.map(source);
        entity.setFilteredActivities(this.astActivityList(source.getActivitiesFilter()));

        return entity;
    }

    private List<Long> asIdList(final List<Activity> filteredActivities) {

        return convert(filteredActivities, Activity::getId);
    }

    private List<Activity> astActivityList(final List<Long> activitiesFilter) {

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

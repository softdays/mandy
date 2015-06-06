package org.softdays.mandy.dto.converter;

import org.dozer.DozerConverter;
import org.softdays.mandy.core.model.ActivityType;

public class ActivityTypeToStringConverter extends
        DozerConverter<ActivityType, String> {

    public ActivityTypeToStringConverter() {
        super(ActivityType.class, String.class);
    }

    @Override
    public String convertTo(final ActivityType source, final String destination) {
        return source.name();
    }

    @Override
    public ActivityType convertFrom(final String source,
            final ActivityType destination) {
        return ActivityType.valueOf(source);
    }

}

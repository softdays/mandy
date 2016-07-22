package org.softdays.mandy.dto.mapping;

import org.softdays.mandy.core.model.Activity;
import org.softdays.mandy.core.model.ActivityCategory;
import org.softdays.mandy.core.model.ActivityType;
import org.softdays.mandy.dto.ActivityDto;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING)
public abstract class ActivityMapper {

    public abstract ActivityDto map(Activity source);

    public abstract Activity map(ActivityDto source);

    public ActivityCategory asActivityCategory(final Character code) {
        return ActivityCategory.fromCode(code);
    }

    public ActivityType asActivityType(final Character code) {
        return ActivityType.fromCode(code);
    }

    public Character asActivityCode(final ActivityCategory category) {
        return category.getPk();
    }

    public Character asActivityCode(final ActivityType type) {
        return type.getPk();
    }
}

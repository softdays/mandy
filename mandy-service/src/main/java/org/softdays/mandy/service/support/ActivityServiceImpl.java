/*
 * MANDY is a simple webapp to track man-day consumption on activities.
 * 
 * Copyright 2014, rpatriarche
 *
 * This file is part of MANDY software.
 *
 * MANDY is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * MANDY is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.softdays.mandy.service.support;

import java.util.List;
import java.util.stream.Collectors;

import org.softdays.mandy.core.model.Activity;
import org.softdays.mandy.core.model.ActivityCategory;
import org.softdays.mandy.core.model.Resource;
import org.softdays.mandy.dao.ActivityDao;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.dto.mapping.ActivityMapper;
import org.softdays.mandy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ActivityServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * Instantiates a new activity service impl.
     */
    public ActivityServiceImpl() {
        super();
    }

    @Override
    public List<ActivityDto> getActivities(final Long userId) {

        List<Activity> activities = this.activityDao.findByResource(new Resource(userId),
                ActivityCategory.PROJECT);

        return activities.stream().map(a -> this.activityMapper.map(a))
                .collect(Collectors.toList());
    }

    @Override
    public ActivityDto createSubActivity(final ActivityDto activityDto) {
        Activity newActivity = this.activityMapper.map(activityDto);

        Integer maxPos = this.activityDao.findMaxPosition(activityDto.getParentActivityId());
        newActivity.setPosition(++maxPos);

        return this.activityMapper.map(this.activityDao.save(newActivity));
    }
}

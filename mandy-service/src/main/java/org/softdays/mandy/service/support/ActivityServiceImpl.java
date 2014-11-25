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

import javax.transaction.Transactional;

import org.softdays.mandy.dao.ActivityDao;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.service.ActivityService;
import org.softdays.mandy.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MapperService utilService;

    /**
     * Instantiates a new activity service impl.
     */
    public ActivityServiceImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ActivityService#getActivities(java.lang.Long)
     */
    @Override
    public List<ActivityDto> getActivities(final Long userId) {

        return this.utilService.map(
        // activityDao.findAll(new Sort(Direction.ASC, "position")),
                this.activityDao.findByResource(userId), ActivityDto.class);
    }
}

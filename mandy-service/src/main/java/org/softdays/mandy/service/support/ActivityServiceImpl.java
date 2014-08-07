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
 * Created by rpatriarche on 09/03/14.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private MapperService utilService;

    public List<ActivityDto> getActivities(Long userId) {

	return utilService.map(
	// activityDao.findAll(new Sort(Direction.ASC, "position")),
		activityDao.findByResource(userId), ActivityDto.class);
    }
}

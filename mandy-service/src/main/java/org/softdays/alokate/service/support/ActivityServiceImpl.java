package org.softdays.alokate.service.support;

import org.dozer.Mapper;
import org.softdays.alokate.dao.ActivityDao;
import org.softdays.alokate.dto.ActivityDto;
import org.softdays.alokate.model.Activity;
import org.softdays.alokate.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpatriarche on 09/03/14.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;

    @Resource
    private Mapper dozerBeanMapper;

    public List<ActivityDto> getActivities() {
        List<Activity> activities = activityDao.findAll();
        List<ActivityDto> dtos = new ArrayList<ActivityDto>(activities.size());
        for(Activity activity : activities) {
            dtos.add(dozerBeanMapper.map(activity, ActivityDto.class));
        }

        return dtos;
    }
}

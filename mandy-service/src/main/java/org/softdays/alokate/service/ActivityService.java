package org.softdays.alokate.service;

import org.softdays.alokate.dto.ActivityDto;
import org.softdays.alokate.model.Activity;

import java.util.List;

/**
 * Created by rpatriarche on 02/03/14.
 */
public interface ActivityService {

    List<ActivityDto> getActivities();
}

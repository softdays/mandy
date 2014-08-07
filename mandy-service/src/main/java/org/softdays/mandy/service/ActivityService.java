package org.softdays.mandy.service;

import java.util.List;

import org.softdays.mandy.dto.ActivityDto;

/**
 * Created by rpatriarche on 02/03/14.
 */
public interface ActivityService {

    /**
     * Returns the activities to which the user is allocated.
     * 
     * @param userId
     *            User (resource) identifier.
     * 
     * @return A list of {@link ActivityDto}
     */
    List<ActivityDto> getActivities(Long userId);
}

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

package org.softdays.mandy.dao;

import java.util.List;

import org.softdays.mandy.core.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public interface ActivityDao extends JpaRepository<Activity, Long> {

    /**
     * Find by resource.
     * 
     * @param userId
     *            the user id
     * @return the list
     */
    @Query("select distinct a from #{#entityName} a left join fetch a.teams t"
            + " where :resourceId in elements(t.resources)"
            + " or a.type <> org.softdays.mandy.core.model.ActivityType.P"
            + " order by a.type asc, a.position asc")
    List<Activity> findByResource(@Param("resourceId") Long userId);

}

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
import org.softdays.mandy.core.model.Resource;
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
     * Utilise une jointure externe pour récupérer les activités génériques hors
     * projets associés à une équipe (absences, formation)
     * 
     * @param userId
     *            the user id
     * @return the list
     * 
     * @see https://hibernate.atlassian.net/browse/HHH-8866
     */
    @Query("select distinct a from #{#entityName} a left join fetch a.teams t"
            + " where :resource in elements(t.resources)" + " or a.category <> 'P'"
            + " order by a.category asc, a.type asc, a.position asc")
    List<Activity> findByResource(@Param("resource") final Resource resource);

    @Query("select max(a.position) from #{#entityName} a"
            + " where a.parentActivity.id=:parentActivityId")
    Integer findMaxPosition(@Param("parentActivityId") final Long parentActivityId);

}

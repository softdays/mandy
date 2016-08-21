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

import java.time.LocalDate;
import java.util.List;

import org.softdays.mandy.core.model.Imputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public interface ImputationDao extends JpaRepository<Imputation, Long> {

    /**
     * Retourne l'ensemble des imputations retrouvées pour l'utilisateur et pour
     * le mois correspondant à la date indiquée.
     * 
     * version using just method name but without fetch:
     * findByResourceIdAndDateBetweenOrderByActivityPositionAscDateAsc
     * 
     * @param userId
     *            the user ID.
     * @param startDate
     *            the date from which we want to retrieve imputation entries.
     * @param endDate
     *            the date until which we want to retrieve imputation entries.
     * 
     * 
     * @return Une liste d'entités @{link Imputation}.
     */
    @Query("select i from #{#entityName} i " + "join fetch i.resource r "
            + "join fetch i.activity a " + "where r.id=:resourceId "
            + "and (i.date between :startDate and :endDate) "
            + "order by a.position asc, i.date asc")
    List<Imputation> findByResourceAndDateRange(@Param("resourceId") Long userId,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

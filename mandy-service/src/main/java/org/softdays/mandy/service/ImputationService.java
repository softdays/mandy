/**
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
package org.softdays.mandy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.softdays.mandy.dto.ImputationDto;

/**
 * Imputation management service.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public interface ImputationService {

    /**
     * Returns existing imputations for the given resource and the month which
     * belongs the given date.
     * 
     * @param resourceId
     *            A resource identifier.
     * @param date
     *            The given date corresponding to the month to consider for
     *            search.
     * @return imputations indexed by activity.id
     */
    Map<Long, List<ImputationDto>> findImputations(Long resourceId, Date date);

    /**
     * Persists a new imputation.
     * 
     * @param newImputation
     *            the new imputation
     * @return the imputation dto
     */
    ImputationDto createImputation(ImputationDto newImputation);

    /**
     * Update the existing imputation.
     * 
     * @param imputation
     *            the imputation
     * 
     * @return The updated imputation dto.
     */
    ImputationDto updateImputation(ImputationDto imputation);

    /**
     * Delete imputation specified by the given id.
     * 
     * @param imputationId
     *            the imputation id
     */
    void deleteImputation(Long imputationId);
}

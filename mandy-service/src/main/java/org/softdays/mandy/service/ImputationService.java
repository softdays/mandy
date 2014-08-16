package org.softdays.mandy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.softdays.mandy.dto.ImputationDto;

/**
 * Imputation management service.
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
     */
    ImputationDto createImputation(ImputationDto newImputation);

    /**
     * Update the existing imputation.
     */
    void updateImputation(ImputationDto imputation);

    /**
     * Delete imputation specified by the given id.
     */
    void deleteImputation(Long imputationId);
}

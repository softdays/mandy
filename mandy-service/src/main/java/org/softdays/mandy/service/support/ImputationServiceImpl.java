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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.softdays.mandy.core.model.Imputation;
import org.softdays.mandy.dao.ImputationDao;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.ImputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ImputationServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
@Transactional
public class ImputationServiceImpl implements ImputationService {

    @Autowired
    private ImputationDao imputationDao;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private Mapper mapper;

    /**
     * Instantiates a new imputation service impl.
     */
    public ImputationServiceImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#findImputations(java.lang
     * .Long, java.util.Date)
     */
    @Override
    public Map<Long, List<ImputationDto>> findImputations(final Long resourceId,
            final LocalDate givenDate) {

        final LocalDate startDate = this.calendarService.getFirstMondayOfMonth(givenDate);
        final LocalDate endDate = this.calendarService.getFirstSundayAfterEndOfMonth(givenDate);

        final List<Imputation> imputations = this.imputationDao
                .findByResourceAndDateRange(resourceId, startDate, endDate);

        final Map<Long, List<ImputationDto>> results = new LinkedHashMap<>();
        // ventiler les imputations pour obtenir des lignes
        for (final Imputation imputation : imputations) {
            final Long activityId = imputation.getActivity().getId();
            final ImputationDto imputationDto = this.mapper.map(imputation, ImputationDto.class);
            if (!results.containsKey(activityId)) {
                results.put(activityId, new ArrayList<ImputationDto>());
            }
            final List<ImputationDto> imputationsDtos = results.get(activityId);
            // les imputations sont préalablement ordonnées chronologiquement
            imputationsDtos.add(imputationDto);
        }

        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#createImputation(org.softdays
     * .mandy.dto.ImputationDto)
     */
    @Override
    public ImputationDto createImputation(final ImputationDto newImputation) {
        final Imputation entity = this.mapper.map(newImputation, Imputation.class);
        final Imputation imputation = this.imputationDao.save(entity);
        return this.mapper.map(imputation, ImputationDto.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#updateImputation(org.softdays
     * .mandy.dto.ImputationDto)
     */
    @Override
    public ImputationDto updateImputation(final ImputationDto imputationDto) {
        final Long imputationId = imputationDto.getImputationId();
        final Imputation imputation = this.imputationDao.findOne(imputationId);
        if (imputation == null) {
            throw new IllegalArgumentException("Imputation ID not found: " + imputationId);
        }
        // on synchronise les champs mutables
        imputation.setQuota(imputationDto.getQuota());
        imputation.setComment(imputationDto.getComment());
        // dirty checking will do the trick
        return this.mapper.map(imputation, ImputationDto.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#deleteImputation(java.lang
     * .Long)
     */
    @Override
    public void deleteImputation(final Long imputationId) {
        this.imputationDao.delete(imputationId);
    }
}

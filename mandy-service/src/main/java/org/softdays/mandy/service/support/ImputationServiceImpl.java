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
package org.softdays.mandy.service.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.softdays.mandy.dao.ImputationDao;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.model.Imputation;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.ImputationService;
import org.softdays.mandy.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MapperService listMapper;

    @Autowired
    private Mapper mapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#findImputations(java.lang
     * .Long, java.util.Date)
     */
    @Override
    public Map<Long, List<ImputationDto>> findImputations(Long resourceId,
	    Date date) {

	Date startDate = calendarService.getFirstMondayOfMonth(date);
	Date endDate = calendarService.getFirstSundayAfterEndOfMonth(date);

	List<Imputation> imputations = imputationDao
		.findByResourceAndDateRange(resourceId, startDate, endDate);

	Map<Long, List<ImputationDto>> results = new LinkedHashMap<>();
	// ventiler les imputations pour obtenir des lignes
	for (Imputation imputation : imputations) {
	    Long activityId = imputation.getActivity().getId();
	    ImputationDto imputationDto = mapper.map(imputation,
		    ImputationDto.class);
	    if (!results.containsKey(activityId)) {
		results.put(activityId, new ArrayList<ImputationDto>());
	    }
	    List<ImputationDto> imputationsDtos = results.get(activityId);
	    // les imputations sont prélablement ordonnées chronologiquement
	    imputationsDtos.add(imputationDto);
	}

	// List<ImputationDto> imputations = utilService.map(imputation,
	// ImputationDto.class);

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
    public ImputationDto createImputation(ImputationDto newImputation) {
	Imputation entity = mapper.map(newImputation, Imputation.class);
	Imputation imputation = imputationDao.save(entity);
	return mapper.map(imputation, ImputationDto.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#updateImputation(org.softdays
     * .mandy.dto.ImputationDto)
     */
    @Override
    public void updateImputation(ImputationDto imputationDto) {
	Long imputationId = imputationDto.getImputationId();
	Imputation imputation = imputationDao.findOne(imputationId);
	if (imputation == null) {
	    throw new IllegalArgumentException("Imputation ID not found: "
		    + imputationId);
	}
	// on synchronise les champs mutables
	imputation.setQuota(imputationDto.getQuota());
	imputation.setComment(imputationDto.getComment());
	// dirty checking will do the trick
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ImputationService#deleteImputation(java.lang
     * .Long)
     */
    @Override
    public void deleteImputation(Long imputationId) {
	imputationDao.delete(imputationId);
    }
}

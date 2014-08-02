package org.softdays.mandy.service.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.softdays.mandy.dao.ImputationDao;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.model.Activity;
import org.softdays.mandy.model.Imputation;
import org.softdays.mandy.service.CalendarService;
import org.softdays.mandy.service.ImputationService;
import org.softdays.mandy.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rpatriarche on 02/03/14.
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

    public Map<ActivityDto, List<ImputationDto>> findImputations(
	    Long resourceId, Date date) {

	Date startDate = calendarService.getFirstMondayBeforeStartOfMonth(date);
	Date endDate = calendarService.getFirstSundayAfterEndOfMonth(date);

	List<Imputation> imputations = imputationDao
		.findByResourceAndDateRange(resourceId, startDate, endDate);

	Map<ActivityDto, List<ImputationDto>> results = new LinkedHashMap<ActivityDto, List<ImputationDto>>();
	// ventiler les imputations pour obtenir des lignes
	for (Imputation imputation : imputations) {
	    Activity activity = imputation.getActivity();
	    ActivityDto activityDto = mapper.map(activity, ActivityDto.class);
	    ImputationDto imputationDto = mapper.map(imputation,
		    ImputationDto.class);
	    if (!results.containsKey(activityDto)) {
		results.put(activityDto, new ArrayList<ImputationDto>());
	    }
	    List<ImputationDto> imputationsDtos = results.get(activityDto);
	    // les imputations sont prélablement ordonnées chronologiquement
	    imputationsDtos.add(imputationDto);
	}

	// List<ImputationDto> imputations = utilService.map(imputation,
	// ImputationDto.class);

	return results;
    }

    @Override
    public List<ImputationDto> saveImputations(List<ImputationDto> imputations) {
	List<Imputation> entities = listMapper.map(imputations,
		Imputation.class);
	entities = imputationDao.save(entities);
	return listMapper.map(imputations, ImputationDto.class);
    }
}

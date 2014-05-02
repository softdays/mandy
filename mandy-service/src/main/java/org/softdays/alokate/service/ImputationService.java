package org.softdays.alokate.service;

import org.softdays.alokate.dto.ImputationDto;
import org.softdays.alokate.model.Imputation;

import java.util.Date;
import java.util.List;

/**
 * Created by rpatriarche on 02/03/14.
 */
public interface ImputationService {

    List<ImputationDto> getImputations(Integer userId, Date date);
}

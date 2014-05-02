package org.softdays.alokate.service.support;

import org.softdays.alokate.dao.ImputationDao;
import org.softdays.alokate.dto.ImputationDto;
import org.softdays.alokate.dto.DtoFactory;
import org.softdays.alokate.service.ImputationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by rpatriarche on 02/03/14.
 */
@Service
@Transactional
public class ImputationServiceImpl implements ImputationService {

    @Resource
    private ImputationDao imputationDao;

    @Resource
    private DtoFactory dtoFactory;

    public List<ImputationDto> getImputations(Integer userId, Date date) {
        return dtoFactory.convert(imputationDao.findImputations(userId, date));
    }
}

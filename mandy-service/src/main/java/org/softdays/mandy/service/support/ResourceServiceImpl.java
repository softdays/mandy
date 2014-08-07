package org.softdays.mandy.service.support;

import org.dozer.Mapper;
import org.softdays.mandy.dao.ResourceDao;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.model.Resource;
import org.softdays.mandy.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private Mapper mapper;

    @Override
    public ResourceDto findByUid(String uid) {
	Resource res = resourceDao.findOneByUid(uid);
	return res == null ? null : mapper.map(res, ResourceDto.class);
    }

    @Override
    public ResourceDto create(String uid, String lastname, String firstname) {
	Resource res = resourceDao.save(new Resource(uid, lastname, firstname));
	return mapper.map(res, ResourceDto.class);
    }

}

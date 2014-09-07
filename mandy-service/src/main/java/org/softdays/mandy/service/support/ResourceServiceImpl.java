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

import org.dozer.Mapper;
import org.softdays.mandy.dao.ResourceDao;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.model.Resource;
import org.softdays.mandy.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ResourceServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private Mapper mapper;

    /* (non-Javadoc)
     * @see org.softdays.mandy.service.ResourceService#findByUid(java.lang.String)
     */
    @Override
    public ResourceDto findByUid(String uid) {
	Resource res = resourceDao.findOneByUid(uid);
	return res == null ? null : mapper.map(res, ResourceDto.class);
    }

    /* (non-Javadoc)
     * @see org.softdays.mandy.service.ResourceService#create(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResourceDto create(String uid, String lastname, String firstname) {
	Resource res = resourceDao.save(new Resource(uid, lastname, firstname));
	return mapper.map(res, ResourceDto.class);
    }

}

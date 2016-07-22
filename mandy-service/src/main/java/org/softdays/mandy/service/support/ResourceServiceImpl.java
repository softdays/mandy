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

import org.softdays.mandy.core.model.Preference;
import org.softdays.mandy.core.model.Resource;
import org.softdays.mandy.dao.PreferencesDao;
import org.softdays.mandy.dao.ResourceDao;
import org.softdays.mandy.dto.PreferencesDto;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.dto.mapping.PreferenceMapper;
import org.softdays.mandy.dto.mapping.ResourceMapper;
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
    private PreferencesDao preferencesDao;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private PreferenceMapper preferenceMapper;

    /**
     * Instantiates a new resource service impl.
     */
    public ResourceServiceImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ResourceService#findByUid(java.lang.String)
     */
    @Override
    public ResourceDto findByUid(final String uid) {
        final Resource res = this.resourceDao.findOneByUid(uid);
        return res == null ? null : this.resourceMapper.map(res);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.softdays.mandy.service.ResourceService#create(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public ResourceDto create(final String uid, final String lastname, final String firstname) {
        final Resource res = this.resourceDao.save(new Resource(uid, lastname, firstname));

        this.preferencesDao.save(new Preference(res));

        return this.resourceMapper.map(res);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.ResourceService#findResourcePreferences(java
     * .lang.Long)
     */
    @Override
    public PreferencesDto findResourcePreferences(final Long resourceId) {
        PreferencesDto dto = null;
        // check if the user has custom preferences
        final Preference preferences = this.preferencesDao.findOne(resourceId);
        if (preferences == null) {
            // initialize with default business values
            dto = new PreferencesDto();
        } else {
            dto = this.preferenceMapper.asDto(preferences);
        }

        return dto;
    }

    @Override
    public PreferencesDto updateResourcePreferences(final PreferencesDto preferencesDto) {
        final Preference preferences = this.preferenceMapper.asEntity(preferencesDto);
        return this.preferenceMapper.asDto(this.preferencesDao.save(preferences));
    }

}

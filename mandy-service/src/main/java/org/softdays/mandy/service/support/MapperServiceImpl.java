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

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.softdays.mandy.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class MapperServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Component
public class MapperServiceImpl implements MapperService {

    @Autowired
    private Mapper mapper;

    /**
     * Instantiates a new mapper service impl.
     */
    public MapperServiceImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.softdays.mandy.service.MapperService#map(java.lang.Iterable,
     * java.lang.Class)
     */
    @Override
    public <T> List<T> map(final Iterable<?> iterable,
            final Class<T> destinationClass) {
        final List<T> result = new ArrayList<T>();
        for (final Object source : iterable) {
            final T dest = this.mapper.map(source, destinationClass);
            result.add(dest);
        }

        return result;
    }

}

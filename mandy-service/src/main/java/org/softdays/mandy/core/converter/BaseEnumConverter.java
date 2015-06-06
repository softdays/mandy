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
package org.softdays.mandy.core.converter;

import java.lang.reflect.ParameterizedType;

import javax.persistence.AttributeConverter;

import org.softdays.mandy.core.PersistentEnum;

/**
 * 
 * @see https://hibernate.atlassian.net/browse/HHH-8854 This bug will force
 *      subclasses to redondly implements AttributeConverter
 * 
 * @author repatriarche
 *
 * @param <T>
 */
/**
 * @author repatriarche *
 * @since 1.3.0
 */
public class BaseEnumConverter<T extends Enum<T> & PersistentEnum> implements
        AttributeConverter<T, Character> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public BaseEnumConverter() {
        // show limitations and drawbacks og this reflection technique:
        // http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.persistentClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public Character convertToDatabaseColumn(final T attribute) {
        return attribute.getId();
    }

    @Override
    public T convertToEntityAttribute(final Character id) {
        return EnumConverterUtil.valueOf(persistentClass, id);
    }

}

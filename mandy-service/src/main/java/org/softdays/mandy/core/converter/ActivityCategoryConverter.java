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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.softdays.mandy.core.model.ActivityCategory;

/**
 * This converter will be automatically scanned by JPA
 * 
 * @author repatriarche
 * 
 * @since 1.3.0
 */
@Converter(autoApply = true)
public class ActivityCategoryConverter extends BaseEnumConverter<ActivityCategory> implements
        AttributeConverter<ActivityCategory, Character> {}

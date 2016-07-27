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
package org.softdays.mandy.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by rpatriarche on 02/03/14.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ImputationDto {

    private Long imputationId;

    private Long activityId;

    private Long resourceId;

    private LocalDate date;

    private Float quota;

    private String comment;

    /**
     * Instantiates a new imputation dto.
     * 
     * @param activityId
     *            the activity id
     * @param resourceId
     *            the resource id
     * @param date
     *            the date
     * @param quota
     *            the quota
     * @param comment
     *            the comment
     */
    public ImputationDto(final Long activityId, final Long resourceId, final LocalDate date,
                         final Float quota, final String comment) {
        super();
        this.activityId = activityId;
        this.resourceId = resourceId;
        this.date = date;
        this.quota = quota;
        this.comment = comment;
    }
}

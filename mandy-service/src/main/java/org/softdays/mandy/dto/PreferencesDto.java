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

import java.util.ArrayList;
import java.util.List;

import org.softdays.mandy.core.model.Quota;

/**
 * DTO des préférences utilisateur.
 */
public class PreferencesDto {

    private Long resourceId;

    private Float granularity = Quota.HALF.floatValue();

    private List<Long> activitiesFilter = new ArrayList<>();

    private boolean enableSubActivities = false;

    public PreferencesDto() {
        super();
    }

    public PreferencesDto(final Long resourceId) {
        this();
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(final Long resourceId) {
        this.resourceId = resourceId;
    }

    public Float getGranularity() {
        return this.granularity;
    }

    public void setGranularity(final Float granularity) {
        this.granularity = granularity;
    }

    public boolean isEnableSubActivities() {
        return enableSubActivities;
    }

    public void setEnableSubActivities(final boolean enableSubActivities) {
        this.enableSubActivities = enableSubActivities;
    }

    public List<Long> getActivitiesFilter() {
        return this.activitiesFilter;
    }

    public void setActivitiesFilter(final List<Long> activitiesFilter) {
        this.activitiesFilter = activitiesFilter;
    }

}

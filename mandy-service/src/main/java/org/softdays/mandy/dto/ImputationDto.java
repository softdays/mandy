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
package org.softdays.mandy.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.softdays.mandy.web.serializer.JsonDateSerializer;

/**
 * Created by rpatriarche on 02/03/14.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
public class ImputationDto {

    private Long imputationId;

    private Long activityId;

    private Long resourceId;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;

    private Float quota;

    private String comment;

    /**
     * Instantiates a new imputation dto.
     */
    public ImputationDto() {
	super();
    }

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
    public ImputationDto(Long activityId, Long resourceId, Date date,
	    Float quota, String comment) {
	super();
	this.activityId = activityId;
	this.resourceId = resourceId;
	this.date = date;
	this.quota = quota;
	this.comment = comment;
    }

    /**
     * Gets the imputation id.
     * 
     * @return the imputation id
     */
    public Long getImputationId() {
	return imputationId;
    }

    /**
     * Sets the imputation id.
     * 
     * @param imputationId
     *            the new imputation id
     */
    public void setImputationId(Long imputationId) {
	this.imputationId = imputationId;
    }

    /**
     * Gets the activity id.
     * 
     * @return the activity id
     */
    public Long getActivityId() {
	return activityId;
    }

    /**
     * Sets the activity id.
     * 
     * @param activityId
     *            the new activity id
     */
    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    /**
     * Gets the resource id.
     * 
     * @return the resource id
     */
    public Long getResourceId() {
	return resourceId;
    }

    /**
     * Sets the resource id.
     * 
     * @param resourceId
     *            the new resource id
     */
    public void setResourceId(Long resourceId) {
	this.resourceId = resourceId;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
	return date;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            the new date
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * Gets the quota.
     * 
     * @return the quota
     */
    public Float getQuota() {
	return quota;
    }

    /**
     * Sets the quota.
     * 
     * @param quota
     *            the new quota
     */
    public void setQuota(Float quota) {
	this.quota = quota;
    }

    /**
     * Gets the comment.
     * 
     * @return the comment
     */
    public String getComment() {
	return comment;
    }

    /**
     * Sets the comment.
     * 
     * @param comment
     *            the new comment
     */
    public void setComment(String comment) {
	this.comment = comment;
    }
}

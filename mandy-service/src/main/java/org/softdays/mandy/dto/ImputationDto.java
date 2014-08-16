package org.softdays.mandy.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.softdays.mandy.web.serializer.JsonDateSerializer;

/**
 * Created by rpatriarche on 02/03/14.
 */
public class ImputationDto {

    private Long imputationId;

    private Long activityId;

    private Long resourceId;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;

    private Float quota;

    private String comment;

    public ImputationDto() {
	super();
    }

    public ImputationDto(Long activityId, Long resourceId, Date date,
	    Float quota, String comment) {
	super();
	this.activityId = activityId;
	this.resourceId = resourceId;
	this.date = date;
	this.quota = quota;
	this.comment = comment;
    }

    public Long getImputationId() {
	return imputationId;
    }

    public void setImputationId(Long imputationId) {
	this.imputationId = imputationId;
    }

    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public Long getResourceId() {
	return resourceId;
    }

    public void setResourceId(Long resourceId) {
	this.resourceId = resourceId;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Float getQuota() {
	return quota;
    }

    public void setQuota(Float quota) {
	this.quota = quota;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }
}

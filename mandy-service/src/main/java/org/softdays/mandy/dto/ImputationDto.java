package org.softdays.mandy.dto;

import java.util.Date;

import org.softdays.mandy.model.Imputation.Quota;

/**
 * Created by rpatriarche on 02/03/14.
 */
public class ImputationDto {

    private Long imputationId;

    private Long activityId;

    private Long resourceId;

    private Date date;

    private Quota quota;

    private String comment;

    public ImputationDto() {
	super();
    }

    public ImputationDto(Long activityId, Long resourceId, Date date,
	    Quota quota, String comment) {
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

    public Quota getQuota() {
	return quota;
    }

    public void setQuota(Quota quota) {
	this.quota = quota;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }
}

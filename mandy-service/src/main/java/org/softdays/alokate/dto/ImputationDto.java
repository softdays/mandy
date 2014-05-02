package org.softdays.alokate.dto;

import org.softdays.alokate.model.Imputation;

import java.util.Date;

/**
 * Created by rpatriarche on 02/03/14.
 */
public class ImputationDto {

    private Long projectId;

    private Date date;

    private Imputation.Quota quota;

    private String comment;

    public ImputationDto() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Imputation.Quota getQuota() {
        return quota;
    }

    public void setQuota(Imputation.Quota quota) {
        this.quota = quota;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

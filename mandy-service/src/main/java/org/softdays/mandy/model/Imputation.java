package org.softdays.mandy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Une imputation permet de tracer la charge des ressources sur les activités.
 * Contrainte unique : une seule imputation possible pour une activité, une
 * ressource et une date donnée.
 * 
 * @author rpatriarche
 */
@Entity
@Table(name = "IMPUTATION", uniqueConstraints = @UniqueConstraint(columnNames = {
	"ACTIVITY_ID", "RESOURCE_ID", "DATE" }))
public class Imputation extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "FK__IMPUTATION__ACTIVITY"))
    @org.hibernate.annotations.ForeignKey(name = "FK__IMPUTATION__ACTIVITY")
    // for hbm2ddl
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "FK__IMPUTATION__RESOURCE"))
    @org.hibernate.annotations.ForeignKey(name = "FK__IMPUTATION__RESOURCE")
    // for hbm2ddl
    private Resource resource;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private Float quota;

    @Column(length = 255)
    private String comment;

    public Imputation() {
	super();
    }

    public Imputation(Long imputationId) {
	this.setId(imputationId);
    }

    public Activity getActivity() {
	return activity;
    }

    public void setActivity(Activity activity) {
	this.activity = activity;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Resource getResource() {
	return resource;
    }

    public void setResource(Resource resource) {
	this.resource = resource;
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

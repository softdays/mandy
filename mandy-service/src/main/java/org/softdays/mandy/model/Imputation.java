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
    @JoinColumn(name = "ACTIVITY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK__IMP__ACTIVITY_ID"))
    @org.hibernate.annotations.ForeignKey(name = "FK__IMP__ACTIVITY_ID")
    // for hbm2ddl
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK__IMP__RESOURCE_ID"))
    @org.hibernate.annotations.ForeignKey(name = "FK__IMP__RESOURCE_ID")
    // for hbm2ddl
    private Resource resource;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private Float quota;

    @Column(length = 255)
    private String comment;

    public Imputation() {
	super();
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

    public Quota getQuota() {
	return Quota.valueOf(this.quota);
    }

    public void setQuota(Quota quota) {
	this.quota = quota.floatValue();
    }

    public Resource getResource() {
	return resource;
    }

    public void setResource(Resource resource) {
	this.resource = resource;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public enum Quota {
	NONE(0f), QUARTER(0.25f), HALF(0.5f), WHOLE(1f);

	private float value;

	private Quota(float value) {
	    this.value = value;
	}

	public Float floatValue() {
	    return value;
	}

	public static Quota valueOf(float value) {
	    Quota result = null;
	    for (Quota quota : Quota.values()) {
		if (quota.value == value) {
		    result = quota;
		    break;
		}
	    }
	    return result;
	}
    }
}

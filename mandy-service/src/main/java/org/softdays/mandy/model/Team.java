package org.softdays.mandy.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "TEAM")
public class Team extends AbstractEntity {

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 10)
    private String code;

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 50)
    private String label;

    @ManyToMany
    @JoinTable(name = "ACTIVITY_TEAM", joinColumns = { @JoinColumn(name = "TEAM_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACTIVITY_ID") })
    @org.hibernate.annotations.ForeignKey(name = "FK__ACTIVITY_TEAM__TEAM", inverseName = "FK__ACTIVITY_TEAM__ACTIVITY")
    // for hbm2ddl
    private Set<Activity> activities;

    @ManyToMany(mappedBy = "teams")
    private Set<Resource> resources;

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public Set<Activity> getActivities() {
	return activities;
    }

    public void setActivities(Set<Activity> activities) {
	this.activities = activities;
    }

    public Set<Resource> getResources() {
	return resources;
    }

    public void setResources(Set<Resource> resources) {
	this.resources = resources;
    }

}

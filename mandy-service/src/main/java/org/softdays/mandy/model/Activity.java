package org.softdays.mandy.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

/**
 * Une activité est l'objet de l'imputation de charge.
 * 
 * @author rpatriarche
 */
@Entity
@Table(name = "ACTIVITY")
public class Activity extends AbstractEntity {

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 10)
    private String shortLabel;

    @NaturalId(mutable = true)
    @Column(nullable = false, length = 50)
    private String longLabel;

    @Column(columnDefinition = "char(1) not null default 'P'")
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private Integer position;

    @ManyToMany(mappedBy = "activities")
    private Set<Team> teams;

    @OneToMany(mappedBy = "activity")
    private Set<Imputation> imputations;

    public Activity() {
	super();
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getShortLabel() {
	return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
	this.shortLabel = shortLabel;
    }

    public String getLongLabel() {
	return longLabel;
    }

    public void setLongLabel(String longLabel) {
	this.longLabel = longLabel;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    public Set<Team> getTeams() {
	return teams;
    }

    public void setTeams(Set<Team> teams) {
	this.teams = teams;
    }

    public Set<Imputation> getImputations() {
	return imputations;
    }

    public void setImputations(Set<Imputation> imputations) {
	this.imputations = imputations;
    }

}

package org.softdays.mandy.model;

public enum ActivityType {

    P("Projet"), A("Absence"), Z("Autre activité");

    private String description;

    ActivityType(String desc) {
	this.description = desc;
    }

    public String getName() {
	return this.name();
    }

    public String getDescription() {
	return description;
    }

}

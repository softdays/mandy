package org.softdays.mandy.dto;

public class ActivityTypeDto {

    private String name;

    private String description;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return "TypeActivityDto [name=" + name + "]";
    }

}

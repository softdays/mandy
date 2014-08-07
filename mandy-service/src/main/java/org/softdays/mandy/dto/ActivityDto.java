package org.softdays.mandy.dto;

/**
 * Created by rpatriarche on 09/03/14.
 */
public class ActivityDto {

    private Long id;

    private String shortLabel;

    private String longLabel;

    private String type;

    public ActivityDto() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
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

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    // nécessaire si utilisation au sein d'une LinkedhashMap
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ActivityDto other = (ActivityDto) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "ActivityDto [id=" + id + ", shortLabel=" + shortLabel
		+ ", type=" + type + "]";
    }

}

package org.softdays.mandy.dto;

/**
 * Created by rpatriarche on 09/03/14.
 */
public class ActivityDto implements Comparable<ActivityDto> {

    private Long id;

    private String label;

    private Integer position;

    public ActivityDto() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    // nécessaire si utilisation au sein d'une TreeMap
    @Override
    public int compareTo(ActivityDto o) {
	return this.position.compareTo(o.position);
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
}

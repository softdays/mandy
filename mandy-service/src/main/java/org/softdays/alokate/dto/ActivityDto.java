package org.softdays.alokate.dto;

/**
 * Created by rpatriarche on 09/03/14.
 */
public class ActivityDto {

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
}

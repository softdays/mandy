package org.softdays.alokate.dto;

/**
 * Created by rpatriarche on 01/03/14.
 */
public class TestDto {

    private Integer id;

    private String label;

    public TestDto() {
    }

    public TestDto(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

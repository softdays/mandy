package org.softdays.alokate.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by rpatriarche on 02/03/14.
 */
@Entity
@Table(name = "ACTIVITY")
public class Activity extends AbstractEntity {

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private String label;

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private Integer position;

    @ManyToMany(mappedBy = "activities")
    private Set<Resource> resources;

    @OneToMany(mappedBy = "activity")
    private Set<Imputation> imputations;

    public Activity() {
        super();
    }

    public Activity(Long id, String label) {
        this.setId(id);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {


        this.resources = resources;
    }

    public Set<Imputation> getImputations() {
        return imputations;
    }

    public void setImputations(Set<Imputation> imputations) {
        this.imputations = imputations;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        if (!super.equals(o)) return false;

        Activity activity = (Activity) o;

        if (label != null ? !label.equals(activity.label) : activity.label != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}

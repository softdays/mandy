package org.softdays.alokate.model;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by rpatriarche on 02/03/14.
 */
@Entity
@Table(name = "RESOURCE")
public class Resource extends AbstractEntity {

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @OneToMany(mappedBy = "resource")
    private Set<Imputation> imputations;

    @ManyToMany
    @JoinTable(name = "ACTIVITY_RESOURCE", joinColumns = {@JoinColumn(name = "RESOURCE_ID")}, inverseJoinColumns = {@JoinColumn(name = "ACTIVITY_ID")})
    @org.hibernate.annotations.ForeignKey(name = "FK__ACTRES__RESOURCE_ID", inverseName = "FK__ACTRES__ACTIVITY_ID") // for hbm2ddl
    private Set<Activity> activities;

    public Resource() {
        super();
    }

    public Resource(Long id, String name, String firstname) {
        this();
        this.lastName = name;
        this.firstName = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<Imputation> getImputations() {
        return imputations;
    }

    public void setImputations(Set<Imputation> imputations) {
        this.imputations = imputations;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;

        Resource resource = (Resource) o;

        if (firstName != null ? !firstName.equals(resource.firstName) : resource.firstName != null) return false;
        if (lastName != null ? !lastName.equals(resource.lastName) : resource.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lastName != null ? lastName.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        return result;
    }
}

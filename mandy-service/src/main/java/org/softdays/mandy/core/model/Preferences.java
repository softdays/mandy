/*
 * MANDY is a simple webapp to track man-day consumption on activities.
 * 
 * Copyright 2014, rpatriarche
 *
 * This file is part of MANDY software.
 *
 * MANDY is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * MANDY is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.softdays.mandy.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.softdays.mandy.core.BaseEqualable;

/**
 * The user preferences entity.
 * 
 * @author rpatriarche
 * @since 1.1.0
 * 
 */
@Entity
@Table(name = "PREFERENCES")
public class Preferences extends BaseEqualable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RESOURCE_ID")
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "RESOURCE_ID")
    @org.hibernate.annotations.ForeignKey(name = "FK__PREFERENCES__RESOURCE")
    private Resource resource;

    /**
     * The user preference for imputation granularity. Must be multiple of 1.
     */
    @Column(columnDefinition = "float NOT NULL default '0.5'")
    private Float granularity = Quota.HALF.floatValue();

    public Preferences() {
        super();
    }

    public Preferences(final Resource resource) {
        this();
        this.setId(resource.getId());
        this.resource = resource;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
        this.resource = id == null ? null : new Resource(id);
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(final Resource resource) {
        this.resource = resource;
        this.id = resource == null ? null : resource.getId();
    }

    public Float getGranularity() {
        return this.granularity;
    }

    public void setGranularity(final Float granularity) {
        this.granularity = granularity;
    }

}

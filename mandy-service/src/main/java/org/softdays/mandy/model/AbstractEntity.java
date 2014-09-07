/**
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
package org.softdays.mandy.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Classe de base pour les entités, gère l'identifiant technique.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(Long id) {
	this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (!(o instanceof AbstractEntity))
	    return false;

	AbstractEntity that = (AbstractEntity) o;

	if (id != null ? !id.equals(that.id) : that.id != null)
	    return false;

	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	return id != null ? id.hashCode() : 0;
    }
}

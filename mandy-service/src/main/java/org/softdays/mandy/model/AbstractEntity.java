package org.softdays.mandy.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Classe de base pour les entités, gère l'identifiant technique.
 * 
 * @author rpatriarche
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

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

    @Override
    public int hashCode() {
	return id != null ? id.hashCode() : 0;
    }
}

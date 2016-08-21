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
package org.softdays.mandy.core;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe de base pour les entités. Gère l'identifiant technique, factorise la
 * base de la méthode equals().
 * 
 * @author rpatriarche
 * @since 1.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class DefaultIdentifiable extends AbstractEqualable implements Identifiable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Override
    protected void businessEquals(final Object obj, final EqualsBuilder equalsBuilder) {
        final Identifiable other = (Identifiable) obj;
        equalsBuilder.append(this.getId(), other.getId());
    }

    @Override
    protected void businessHashCode(final HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getId());
    }

}

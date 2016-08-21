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

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import lombok.NoArgsConstructor;

/**
 * This base class allows to compare to objects in terms of technical and
 * business logic.
 * 
 * @author rpatriarche
 * @since 1.0.1
 */
@NoArgsConstructor
public abstract class AbstractEqualable implements Equalable, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public final EqualStatus equalsConsideringTechnicalLogic(final Object obj) {
        if (obj == null) {
            return EqualStatus.NOT_EQUAL;
        }
        if (obj == this) {
            return EqualStatus.EQUAL;
        }

        return obj.getClass() == this.getClass() ? EqualStatus.UNKNOWN : EqualStatus.NOT_EQUAL;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(super.hashCode());
        this.businessHashCode(hashCodeBuilder);

        return hashCodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result;
        EqualStatus status = this.equalsConsideringTechnicalLogic(obj);
        if (status == EqualStatus.UNKNOWN) {
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            this.businessEquals(obj, equalsBuilder);
            result = equalsBuilder.isEquals();
        } else {
            result = status == EqualStatus.EQUAL ? Boolean.TRUE : Boolean.FALSE;
        }

        return result;
    }

    /**
     * Performs business hash code.
     *
     * @param hashCodeBuilder
     *            the {@link HashCodeBuilder} instance to use to perform comparison
     */
    protected abstract void businessHashCode(HashCodeBuilder hashCodeBuilder);

    /**
     * Performs business comparison.
     *
     * @param obj
     *            the obj to compare with
     * @param equalsBuilder
     *            the {@link EqualsBuilder} instance to use to perfom comparison
     */
    protected abstract void businessEquals(final Object obj, final EqualsBuilder equalsBuilder);

}

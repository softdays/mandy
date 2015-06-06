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
package org.softdays.mandy.core.converter;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.softdays.mandy.core.PersistentEnum;

/**
 * Base class for serializing enum through JPA.
 * 
 * @author repatriarche
 * 
 * @since 1.3.0
 */
public abstract class PersistentEnumUserType<T extends PersistentEnum> implements UserType {

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(final Object x, final Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(final Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names,
            final SessionImplementor session, final Object owner) throws HibernateException,
            SQLException {
        int id = rs.getInt(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        for (PersistentEnum value : returnedClass().getEnumConstants()) {
            if (id == value.getId()) {
                return value;
            }
        }
        throw new IllegalStateException("Unknown " + returnedClass().getSimpleName() + " id");
    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index,
            final SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.CHAR);
        } else {
            st.setString(index, String.valueOf(((PersistentEnum) value).getId()));
        }

    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner)
            throws HibernateException {
        return original;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.CHAR };
    }

    @Override
    public abstract Class<T> returnedClass();

}

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

package org.softdays.mandy.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ninja_squad.dbsetup.destination.Destination;

/**
 * A DbSetup destination which wraps a DataSource and gets its connection from a
 * JDBC DataSource, adding awareness of Spring-managed transactions.
 * 
 * @see https://gist.githubusercontent.com/arey/6453086/raw/
 *      e83507ca88a2edf7ee1f90cc9054fb6640e83c59
 *      /TransactionAwareDestination.java
 */
public class TransactionAwareDestination implements Destination {

    private final DataSource dataSource;

    private final PlatformTransactionManager transactionManager;

    /**
     * Create a new TransactionAwareDestination.
     * 
     * @param dataSource
     *            the target DataSource
     * @param transactionManager
     *            the transaction manager to use
     */
    public TransactionAwareDestination(final DataSource dataSource,
            final PlatformTransactionManager transactionManager) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
        this.transactionManager = transactionManager;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return (Connection) Proxy.newProxyInstance(
                ConnectionProxy.class.getClassLoader(),
                new Class[] { ConnectionProxy.class },
                new TransactionAwareInvocationHandler(this.dataSource));

    }

    private class TransactionAwareInvocationHandler extends
            DefaultTransactionDefinition implements InvocationHandler {

        private final DataSource targetDataSource;

        public TransactionAwareInvocationHandler(
                final DataSource targetDataSource) {
            this.targetDataSource = targetDataSource;
        }

        @Override
        public Object invoke(final Object proxy, final Method method,
                final Object[] args) throws Throwable {

            if (method.getName().equals("commit")) {
                final TransactionStatus status = TransactionAwareDestination.this.transactionManager
                        .getTransaction(this);
                TransactionAwareDestination.this.transactionManager
                        .commit(status);
                return null;
            } else if (method.getName().equals("rollback")) {
                final TransactionStatus status = TransactionAwareDestination.this.transactionManager
                        .getTransaction(this);
                TransactionAwareDestination.this.transactionManager
                        .rollback(status);
                return null;
            } else {
                try {
                    final Connection connection = this.targetDataSource
                            .getConnection();
                    final Object retVal = method.invoke(connection, args);
                    return retVal;
                } catch (final InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "TransactionAwareDestination{" + "dataSource=" + this.dataSource
                + ", transactionManager=" + this.transactionManager + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final TransactionAwareDestination that = (TransactionAwareDestination) o;

        if (this.dataSource != null ? !this.dataSource.equals(that.dataSource)
                : that.dataSource != null) {
            return false;
        }
        if (this.transactionManager != null ? !this.transactionManager
                .equals(that.transactionManager)
                : that.transactionManager != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.dataSource != null ? this.dataSource.hashCode() : 0;
        result = 31
                * result
                + (this.transactionManager != null ? this.transactionManager
                        .hashCode() : 0);
        return result;
    }
}

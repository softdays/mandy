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
    public TransactionAwareDestination(DataSource dataSource,
	    PlatformTransactionManager transactionManager) {
	this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
	this.transactionManager = transactionManager;
    }

    @Override
    public Connection getConnection() throws SQLException {
	return (Connection) Proxy.newProxyInstance(
		ConnectionProxy.class.getClassLoader(),
		new Class[] { ConnectionProxy.class },
		new TransactionAwareInvocationHandler(dataSource));

    }

    private class TransactionAwareInvocationHandler extends
	    DefaultTransactionDefinition implements InvocationHandler {

	private final DataSource targetDataSource;

	public TransactionAwareInvocationHandler(DataSource targetDataSource) {
	    this.targetDataSource = targetDataSource;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

	    if (method.getName().equals("commit")) {
		TransactionStatus status = transactionManager
			.getTransaction(this);
		transactionManager.commit(status);
		return null;
	    } else if (method.getName().equals("rollback")) {
		TransactionStatus status = transactionManager
			.getTransaction(this);
		transactionManager.rollback(status);
		return null;
	    } else
		try {
		    Connection connection = targetDataSource.getConnection();
		    Object retVal = method.invoke(connection, args);
		    return retVal;
		} catch (InvocationTargetException ex) {
		    throw ex.getTargetException();
		}
	}
    }

    @Override
    public String toString() {
	return "TransactionAwareDestination{" + "dataSource=" + dataSource
		+ ", transactionManager=" + transactionManager + '}';
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;

	TransactionAwareDestination that = (TransactionAwareDestination) o;

	if (dataSource != null ? !dataSource.equals(that.dataSource)
		: that.dataSource != null)
	    return false;
	if (transactionManager != null ? !transactionManager
		.equals(that.transactionManager)
		: that.transactionManager != null)
	    return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = dataSource != null ? dataSource.hashCode() : 0;
	result = 31
		* result
		+ (transactionManager != null ? transactionManager.hashCode()
			: 0);
	return result;
    }
}

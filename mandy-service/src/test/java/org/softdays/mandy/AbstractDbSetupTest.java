package org.softdays.mandy;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.softdays.mandy.config.SpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public abstract class AbstractDbSetupTest {

    public static final String RESULT_TABLE_NAME = "check";

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected IDatabaseTester databaseTester;

    // the tracker is static because JUnit uses a separate Test instance for
    // every test method.
    public static DbSetupTracker dbSetupTracker = new DbSetupTracker();

    public DbSetup createDbSetup(Operation operation) {
	return new DbSetup(new DataSourceDestination(dataSource), operation);
    }

    public ITable query(String sql) {
	ITable table = null;
	try {
	    table = databaseTester.getConnection().createQueryTable(
		    RESULT_TABLE_NAME, sql);
	    return table;
	} catch (Exception e) {
	    Assert.fail(e.getMessage());
	}
	return null;
    }

    public void execute(String sql) {
	try {
	    Connection c = databaseTester.getConnection().getConnection();
	    Statement s = c.createStatement();
	    s.executeUpdate(sql);
	    s.close();
	} catch (Exception e) {
	    Assert.fail(e.getMessage());
	}
    }

}

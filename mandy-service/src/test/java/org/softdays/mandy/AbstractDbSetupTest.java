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

package org.softdays.mandy;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
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

    public DbSetup createDbSetup(final Operation operation) {
        return new DbSetup(new DataSourceDestination(this.dataSource),
                operation);
    }

    public void execute(final String sql) {
        try {
            final Connection c =
                    this.databaseTester.getConnection().getConnection();
            final Statement s = c.createStatement();
            s.executeUpdate(sql);
            s.close();
        } catch (final Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public ITable query(final String sql) {
        ITable table = null;
        try {
            table =
                    this.databaseTester.getConnection().createQueryTable(
                            RESULT_TABLE_NAME, sql);
            return table;
        } catch (final Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public Float getFirstRowAsFloat(final ITable table, final String column) {
        try {
            return ((Double) table.getValue(0, column)).floatValue();
        } catch (final DataSetException e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public Long getFirstRowAsLong(final ITable table, final String column) {
        try {
            final BigInteger bigInt = (BigInteger) table.getValue(0, column);
            return Long.valueOf(bigInt.longValue());
        } catch (final DataSetException e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }
}

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

package org.softdays.mandy.service.support;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.core.model.Quota;
import org.softdays.mandy.core.model.Resource.Role;
import org.softdays.mandy.dto.PreferencesDto;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

public class ResourceServiceTest extends AbstractDbSetupTest {

    @Autowired
    private ResourceService resourceService;

    @Before
    public void initDataset() {
        // Prepare
        final Operation operation =
                sequenceOf(CommonOperations.DELETE_ALL,
                        CommonOperations.INSERT_REFERENCE_RESOURCES,
                        CommonOperations.INSERT_PREFERENCES);
        final DbSetup dbSetup = this.createDbSetup(operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void findByUidSouldReturnUserRpa() {
        final ResourceDto user =
                this.resourceService.findByUid(CommonOperations.UID_RPA);
        Assert.assertEquals(CommonOperations.ID_RPA, user.getResourceId());
    }

    @Test
    public void findByUidSouldReturnNull() {
        final ResourceDto user = this.resourceService.findByUid("unknown");
        Assert.assertNull(user);
    }

    @Test
    public void createNewResourceShouldCreatePreferencesAlso() {
        final ITable tMax = this.query("select max(id) from mandy.resource");
        Long lastInsertId = this.getFirstRowAsLong(tMax, "max(id)");
        final Long newUserId = new Long(++lastInsertId);

        final ResourceDto user =
                this.resourceService.create("login", "lastname", "firstname");

        Assert.assertNotNull(user);
        Assert.assertEquals(newUserId, user.getResourceId());
        Assert.assertEquals(Role.ROLE_USER.name(), user.getRole());

        final StringBuilder sql =
                new StringBuilder("select * from mandy.preferences").append(
                        " where resource_id=").append(user.getResourceId());

        final ITable table = this.query(sql.toString());
        Assert.assertEquals(1, table.getRowCount());
        Assert.assertEquals(newUserId,
                this.getFirstRowAsLong(table, "resource_id"));
        final Float value = this.getFirstRowAsFloat(table, "granularity");
        Assert.assertEquals(Quota.HALF.floatValue(), value);
    }

    @Test
    public void findPreferencesShouldReturnNotNullDtoInitWithDefaultValues() {
        // this user has no preferences stored
        final PreferencesDto prefs =
                this.resourceService
                        .findResourcePreferences(CommonOperations.ID_RPA);
        Assert.assertNotNull(prefs);
        Assert.assertEquals(Quota.HALF.floatValue(), prefs.getGranularity());
    }

    @Test
    public void findPreferencesShouldReturnNotNullWithHalfGranularity() {
        final PreferencesDto prefs =
                this.resourceService
                        .findResourcePreferences(CommonOperations.ID_CHO);
        Assert.assertNotNull(prefs);
        Assert.assertEquals(Quota.HALF.floatValue(), prefs.getGranularity());
    }

    @Test
    public void findPreferencesShouldReturnNotNullWithQuarterGranularity() {
        final PreferencesDto prefs =
                this.resourceService
                        .findResourcePreferences(CommonOperations.ID_LMO);
        Assert.assertNotNull(prefs);
        Assert.assertEquals(Quota.QUARTER.floatValue(), prefs.getGranularity());

    }

    @Test
    public void updatePreferencesShouldModifyGranularity() {
        final StringBuilder sql =
                new StringBuilder(
                        "select * from mandy.preferences where resource_id=")
                        .append(CommonOperations.ID_CHO);
        ITable table = this.query(sql.toString());
        try {
            final Float value =
                    ((Double) table.getValue(0, "granularity")).floatValue();
            Assert.assertEquals(Quota.HALF.floatValue(), value);
        } catch (final DataSetException e) {
            Assert.fail(e.getMessage());
        }

        final PreferencesDto userPrefs =
                new PreferencesDto(CommonOperations.ID_CHO);
        userPrefs.setGranularity(Quota.QUARTER.floatValue());
        final PreferencesDto dto =
                this.resourceService.updateResourcePreferences(userPrefs);
        Assert.assertNotNull(dto);

        table = this.query(sql.toString());
        final Float value = this.getFirstRowAsFloat(table, "granularity");
        Assert.assertEquals(Quota.QUARTER.floatValue(), value);
    }

    @Test
    public void updatePreferencesShouldCreatePreferenceBecauseThereIsNoPreferencesForThisUser() {
        final StringBuilder sql =
                new StringBuilder(
                        "select * from mandy.preferences where resource_id=")
                        .append(CommonOperations.ID_RPA);
        ITable table = this.query(sql.toString());
        Assert.assertEquals(0, table.getRowCount());

        final PreferencesDto userPrefs =
                new PreferencesDto(CommonOperations.ID_RPA);
        userPrefs.setGranularity(Quota.QUARTER.floatValue());
        final PreferencesDto dto =
                this.resourceService.updateResourcePreferences(userPrefs);
        Assert.assertNotNull(dto);

        Assert.assertEquals(CommonOperations.ID_RPA, dto.getResourceId());

        Assert.assertEquals(Quota.QUARTER.floatValue(), dto.getGranularity());

        table = this.query(sql.toString());
        Assert.assertEquals(1, table.getRowCount());
        final Float value = this.getFirstRowAsFloat(table, "granularity");
        Assert.assertEquals(Quota.QUARTER.floatValue(), value);
    }

    @Test
    public void updatePreferencesShouldNotDoAnythingBecauseThereIsNoChange() {
        final StringBuilder sql =
                new StringBuilder(
                        "select * from mandy.preferences where resource_id=")
                        .append(CommonOperations.ID_RPA);
        final ITable table1 = this.query(sql.toString());
        final PreferencesDto userPrefs =
                new PreferencesDto(CommonOperations.ID_CHO);
        final PreferencesDto dto =
                this.resourceService.updateResourcePreferences(userPrefs);
        Assert.assertNotNull(dto);

        final ITable table2 = this.query(sql.toString());

        try {
            Assertion.assertEquals(table1, table2);
        } catch (final DatabaseUnitException e) {
            Assert.fail(e.getMessage());
        }

    }

}

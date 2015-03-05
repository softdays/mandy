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

import java.util.List;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.dao.ActivityDao;
import org.softdays.mandy.dataset.CommonOperations;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

public class ActivityServiceTest extends AbstractDbSetupTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityDao activityDao;

    @Before
    public void initDataset() {
        // Prepare
        final Operation operation = sequenceOf(CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_REFERENCE_ACTIVITIES,
                CommonOperations.INSERT_REFERENCE_TEAM,
                CommonOperations.INSERT_REFERENCE_ACTIVITY_TEAM,
                CommonOperations.INSERT_REFERENCE_RESOURCES,
                CommonOperations.INSERT_REFERENCE_TEAM_RESOURCE);
        final DbSetup dbSetup = this.createDbSetup(operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void getActivitiesForRpa() {
        // mark this test as read-only test
        dbSetupTracker.skipNextLaunch();
        final ITable table = this
                .query("select * from mandy.activity where type<>'P'");
        Assert.assertEquals(2, table.getRowCount());
        final List<ActivityDto> activities = this.activityService
                .getActivities(CommonOperations.ID_RPA);
        Assert.assertEquals(6, activities.size());
        // check first
        Assert.assertEquals(CommonOperations.ACTIVITY_CP_LABEL,
                activities.get(0).getLongLabel());
        // check last
        Assert.assertEquals(CommonOperations.ACTIVITY_LSUN_LABEL, activities
                .get(activities.size() - 1).getLongLabel());
    }

    @Test
    public void getActivitiesForUnkwown() {
        // mark this test as read-only test
        dbSetupTracker.skipNextLaunch();
        final List<ActivityDto> activities = this.activityService
                .getActivities(0L);
        Assert.assertEquals(2, activities.size());
    }

    @Test
    public void checkCurrentData() {
        final ITable table = this.query("select * from mandy.activity");
        Assert.assertEquals(6, table.getRowCount());
    }
}

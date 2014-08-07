package org.softdays.mandy.service.support;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import java.util.List;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.dao.ActivityDao;
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
	Operation operation = sequenceOf(CommonOperations.DELETE_ALL,
		CommonOperations.INSERT_REFERENCE_ACTIVITIES,
		CommonOperations.INSERT_REFERENCE_TEAM,
		CommonOperations.INSERT_REFERENCE_ACTIVITY_TEAM,
		CommonOperations.INSERT_REFERENCE_RESOURCES,
		CommonOperations.INSERT_REFERENCE_TEAM_RESOURCE);
	DbSetup dbSetup = createDbSetup(operation);
	dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void getActivitiesForRpa() {
	// mark this test as read-only test
	dbSetupTracker.skipNextLaunch();
	ITable table = this
		.query("select * from mandy.activity where type<>'P'");
	Assert.assertEquals(2, table.getRowCount());
	List<ActivityDto> activities = activityService
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
    public void getActivitiesForUnkwonn() {
	// mark this test as read-only test
	dbSetupTracker.skipNextLaunch();
	List<ActivityDto> activities = activityService.getActivities(0L);
	Assert.assertEquals(2, activities.size());
    }

    @Test
    public void checkCurrentData() {
	ITable table = this.query("select * from mandy.activity");
	Assert.assertEquals(6, table.getRowCount());
    }
}

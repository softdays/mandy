package org.softdays.mandy.service.support;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import java.util.List;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.dto.ActivityDto;
import org.softdays.mandy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

public class ActivityServiceTest extends AbstractDbSetupTest {

    @Autowired
    private ActivityService activityService;

    @Before
    public void initDataset() {
	// Prepare
	Operation operation = sequenceOf(CommonOperations.DELETE_ALL,
		CommonOperations.INSERT_REFERENCE_ACTIVITIES);
	DbSetup dbSetup = createDbSetup(operation);
	dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void getActivities() {
	// mark this test as read-only test
	dbSetupTracker.skipNextLaunch();
	List<ActivityDto> activities = activityService.getActivities();
	Assert.assertEquals(6, activities.size());
	// check first
	Assert.assertEquals(CommonOperations.ACTIVITY_CP_LABEL,
		activities.get(0).getLabel());
	// check last
	Assert.assertEquals(CommonOperations.ACTIVITY_LSUN_LABEL, activities
		.get(activities.size() - 1).getLabel());
    }

    @Test
    @Ignore
    public void checkCurrentData() {
	ITable table = this.query("select * from mandy.activity");
	Assert.assertEquals(2, table.getRowCount());
    }
}

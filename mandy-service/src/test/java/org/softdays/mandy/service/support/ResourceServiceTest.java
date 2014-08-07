package org.softdays.mandy.service.support;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.dto.ResourceDto;
import org.softdays.mandy.model.Resource.Role;
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
	Operation operation = sequenceOf(CommonOperations.DELETE_ALL,
		CommonOperations.INSERT_REFERENCE_RESOURCES);
	DbSetup dbSetup = createDbSetup(operation);
	dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void findByUidSouldReturnUserRpa() {
	ResourceDto user = resourceService.findByUid(CommonOperations.UID_RPA);
	Assert.assertEquals(CommonOperations.ID_RPA, user.getResourceId());
    }

    @Test
    public void findByUidSouldReturnNull() {
	ResourceDto user = resourceService.findByUid("unknown");
	Assert.assertNull(user);
    }

    @Test
    public void findByUidSouldCreateNewResource() {
	ResourceDto user = resourceService.create("login", "lastname",
		"firstname");
	Assert.assertNotNull(user);
	Assert.assertEquals(Long.valueOf(7L), user.getResourceId());
	Assert.assertEquals(Role.ROLE_USER.name(), user.getRole());
    }

}

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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.core.model.Resource.Role;
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
        final Operation operation = sequenceOf(CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_REFERENCE_RESOURCES);
        final DbSetup dbSetup = this.createDbSetup(operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void findByUidSouldReturnUserRpa() {
        final ResourceDto user = this.resourceService
                .findByUid(CommonOperations.UID_RPA);
        Assert.assertEquals(CommonOperations.ID_RPA, user.getResourceId());
    }

    @Test
    public void findByUidSouldReturnNull() {
        final ResourceDto user = this.resourceService.findByUid("unknown");
        Assert.assertNull(user);
    }

    @Test
    public void findByUidSouldCreateNewResource() {
        final ResourceDto user = this.resourceService.create("login",
                "lastname", "firstname");
        Assert.assertNotNull(user);
        Assert.assertEquals(Long.valueOf(7L), user.getResourceId());
        Assert.assertEquals(Role.ROLE_USER.name(), user.getRole());
    }

}

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

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.core.model.Quota;
import org.softdays.mandy.dataset.CommonOperations;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.service.ImputationService;
import org.springframework.beans.factory.annotation.Autowired;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.operation.Operation;

public class ImputationServiceTest extends AbstractDbSetupTest {

    private static final Long IMPUT_ID_6 = 6L;
    private static final Long IMPUT_ID_5 = 5L;
    private static final Long IMPUT_ID_4 = 4L;
    private static final Long IMPUT_ID_3 = 3L;
    private static final Long IMPUT_ID_2 = 2L;
    private static final Long IMPUT_ID_1 = 1L;
    @Autowired
    private ImputationService imputationService;

    @Before
    public void initDataset() {
        // Prepare
        final Operation operation = sequenceOf(CommonOperations.DELETE_ALL,
                CommonOperations.INSERT_REFERENCE_ACTIVITIES,
                CommonOperations.INSERT_REFERENCE_RESOURCES,
                insertInto(CommonOperations.DEFAULT_SCHEMA + "IMPUTATION")
                        // .withGeneratedValue("ID", ValueGenerators.sequence())

                        .columns("ID", "RESOURCE_ID", "ACTIVITY_ID", "DATE", "QUOTA", "COMMENT")

                        .values(IMPUT_ID_1, CommonOperations.ID_CHO,
                                CommonOperations.ACTIVITY_LSL_ID, LocalDate.of(2014, 7, 8),
                                Quota.WHOLE.floatValue(), null)

                        .values(IMPUT_ID_2, CommonOperations.ID_CHO,
                                CommonOperations.ACTIVITY_LSL_ID, LocalDate.of(2014, 7, 10),
                                Quota.HALF.floatValue(), "reprise du mantis 546387")

                        .values(IMPUT_ID_3, CommonOperations.ID_LMO,
                                CommonOperations.ACTIVITY_VIESCO_ID, LocalDate.of(2014, 7, 9),
                                Quota.HALF.floatValue(), "analyse modèle de données")

                        .values(IMPUT_ID_4, CommonOperations.ID_CHO,
                                CommonOperations.ACTIVITY_VIESCO_ID, LocalDate.of(2014, 7, 9),
                                Quota.HALF.floatValue(), "analyse modèle de données")

                        // celle ci-dessous doit pas être prise en compte car le
                        // mois est calculé en semaines pleines à partir du
                        // premier lundi du mois, soit pour juillet 2014 une
                        // date de départ au lundi 7/7/14
                        .values(IMPUT_ID_5, CommonOperations.ID_CHO,
                                CommonOperations.ACTIVITY_LSL_ID, LocalDate.of(2014, 7, 3),
                                Quota.WHOLE.floatValue(),
                                "étude technique lot 2\\nédition en masse")

                        .values(IMPUT_ID_6, CommonOperations.ID_CHO,
                                CommonOperations.ACTIVITY_EM_ID, LocalDate.of(2014, 7, 7),
                                Quota.WHOLE.floatValue(), "Tess -> Angine")

                        .build());
        final DbSetup dbSetup = this.createDbSetup(operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void findImputations() {
        dbSetupTracker.skipNextLaunch();
        final LocalDate date = LocalDate.of(2014, 7, 16);
        final Map<Long, List<ImputationDto>> results = this.imputationService
                .findImputations(CommonOperations.ID_CHO, date);

        // trois activités : EM, VIESCO, LSL
        Assert.assertEquals(3, results.keySet().size());
        // 6 imputations insérées dont 5 concernant Christophe et, dans ces 5,
        // 1 est hors du mois à considérer
        // check order
        // viessco item should be first
        final Iterator<Long> iterator = results.keySet().iterator();
        // EM
        List<ImputationDto> imputations = results.get(iterator.next());
        Assert.assertEquals(1, imputations.size());
        Assert.assertEquals(IMPUT_ID_6, imputations.get(0).getImputationId());
        // VIESCO
        imputations = results.get(iterator.next());
        Assert.assertEquals(1, imputations.size());
        Assert.assertEquals(IMPUT_ID_4, imputations.get(0).getImputationId());
        Assert.assertEquals(CommonOperations.ID_CHO, imputations.get(0).getResourceId());
        // LSL
        imputations = results.get(iterator.next());
        Assert.assertEquals(2, imputations.size());
        Assert.assertEquals(IMPUT_ID_1, imputations.get(0).getImputationId());
        Assert.assertEquals(IMPUT_ID_2, imputations.get(1).getImputationId());
    }

    @Test
    public void createImputation() {
        final ImputationDto i = new ImputationDto(CommonOperations.ACTIVITY_LSL_ID,
                CommonOperations.ID_CHO, LocalDate.of(2014, 7, 15), Quota.WHOLE.floatValue(),
                "un commentaire");

        final ImputationDto created = this.imputationService.createImputation(i);

        Assert.assertNotNull(created.getImputationId());

        final StringBuilder sql = new StringBuilder(
                "select * from " + CommonOperations.DEFAULT_SCHEMA + "imputation")
                        .append(" where date='2014-07-15'").append(" and resource_id=")
                        .append(CommonOperations.ID_CHO).append(" and activity_id=")
                        .append(CommonOperations.ACTIVITY_LSL_ID).append(" and quota=1.0")
                        .append(" and comment='un commentaire'");

        final ITable table = this.query(sql.toString());

        Assert.assertEquals(1, table.getRowCount());
    }

    @Test
    public void updateImputation() {
        final ImputationDto merge = new ImputationDto();
        merge.setImputationId(IMPUT_ID_1);
        merge.setResourceId(CommonOperations.ID_CHO);
        final LocalDate dateImputation1 = LocalDate.of(2014, 7, 8);
        merge.setDate(dateImputation1);
        merge.setQuota(Quota.QUARTER.floatValue());// modif here
        merge.setActivityId(CommonOperations.ACTIVITY_LSL_ID);
        merge.setComment("not null comment"); // and here

        this.imputationService.updateImputation(merge);

        final ITable table = this.query("select * from " + CommonOperations.DEFAULT_SCHEMA
                + "imputation where id=" + IMPUT_ID_1);
        try {
            // est-ce que la quota a bien été mis à jour ?
            Object value = table.getValue(0, "quota");
            final Float quota;
            if (value instanceof Double) { // for H2
                quota = ((Double) value).floatValue();
            } else { // for MySQL
                quota = (Float) value;
            }
            Assert.assertEquals(Quota.QUARTER.floatValue(), quota);
            // est-ce que le commentaire à bien été mis à jour ?
            final String comment = (String) table.getValue(0, "comment");
            Assert.assertEquals("not null comment", comment);
            // est-ce que les données existantes n'ont pas été altérées ?
            final Long resourceId = ((BigInteger) table.getValue(0, "resource_id")).longValue();
            Assert.assertEquals(CommonOperations.ID_CHO, resourceId);
            final Long activityId = ((BigInteger) table.getValue(0, "activity_id")).longValue();
            Assert.assertEquals(CommonOperations.ACTIVITY_LSL_ID, activityId);
            final Date date = (Date) table.getValue(0, "date");
            Assert.assertEquals(Date.valueOf(dateImputation1), date);
        } catch (final DataSetException e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void deleteImputation() {
        StringBuilder sql = new StringBuilder(
                "insert into " + CommonOperations.DEFAULT_SCHEMA + "imputation")
                        .append(" (id, activity_id, resource_id, date, quota, comment)")
                        .append(" values(").append("1111,").append(CommonOperations.ACTIVITY_LSL_ID)
                        .append(",").append(CommonOperations.ID_CHO).append(",")
                        .append("'2014-07-15',").append("1.0,").append("'foo bar')");

        this.execute(sql.toString());

        sql = new StringBuilder(
                "select * from " + CommonOperations.DEFAULT_SCHEMA + "imputation where id=1111");
        ITable table = this.query(sql.toString());
        Assert.assertEquals(1, table.getRowCount());

        this.imputationService.deleteImputation(1111L);

        sql = new StringBuilder(
                "select * from " + CommonOperations.DEFAULT_SCHEMA + "imputation where id=1111");
        table = this.query(sql.toString());
        Assert.assertEquals(0, table.getRowCount());

    }
}

package org.softdays.mandy.service.support;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.softdays.mandy.AbstractDbSetupTest;
import org.softdays.mandy.dto.ImputationDto;
import org.softdays.mandy.model.Quota;
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
	Operation operation = sequenceOf(
		CommonOperations.DELETE_ALL,
		CommonOperations.INSERT_REFERENCE_ACTIVITIES,
		CommonOperations.INSERT_REFERENCE_RESOURCES,
		insertInto("MANDY.IMPUTATION")
			// .withGeneratedValue("ID", ValueGenerators.sequence())

			.columns("ID", "RESOURCE_ID", "ACTIVITY_ID", "DATE",
				"QUOTA", "COMMENT")

			.values(IMPUT_ID_1, CommonOperations.ID_CHO,
				CommonOperations.ACTIVITY_LSL_ID,
				new DateTime(2014, 7, 8, 0, 0).toDate(),
				Quota.WHOLE.floatValue(), null)

			.values(IMPUT_ID_2, CommonOperations.ID_CHO,
				CommonOperations.ACTIVITY_LSL_ID,
				new DateTime(2014, 7, 10, 0, 0).toDate(),
				Quota.HALF.floatValue(),
				"reprise du mantis 546387")

			.values(IMPUT_ID_3, CommonOperations.ID_LMO,
				CommonOperations.ACTIVITY_VIESCO_ID,
				new DateTime(2014, 7, 9, 0, 0).toDate(),
				Quota.HALF.floatValue(),
				"analyse modèle de données")

			.values(IMPUT_ID_4, CommonOperations.ID_CHO,
				CommonOperations.ACTIVITY_VIESCO_ID,
				new DateTime(2014, 7, 9, 0, 0).toDate(),
				Quota.HALF.floatValue(),
				"analyse modèle de données")

			// celle ci-dessous doit pas être prise en compte car le
			// mois est calculé en semaines pleines à partir du
			// premier lundi du mois, soit pour juillet 2014 une
			// date de départ au lundi 7/7/14
			.values(IMPUT_ID_5, CommonOperations.ID_CHO,
				CommonOperations.ACTIVITY_LSL_ID,
				new DateTime(2014, 7, 3, 0, 0).toDate(),
				Quota.WHOLE.floatValue(),
				"étude technique lot 2\\nédition en masse")

			.values(IMPUT_ID_6, CommonOperations.ID_CHO,
				CommonOperations.ACTIVITY_EM_ID,
				new DateTime(2014, 7, 7, 0, 0).toDate(),
				Quota.WHOLE.floatValue(), "Tess -> Angine")

			.build());
	DbSetup dbSetup = createDbSetup(operation);
	dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void findImputations() {
	dbSetupTracker.skipNextLaunch();
	Date date = new DateTime(2014, 7, 16, 0, 0).toDate();
	Map<Long, List<ImputationDto>> results = imputationService
		.findImputations(CommonOperations.ID_CHO, date);

	// trois activités : EM, VIESCO, LSL
	Assert.assertEquals(3, results.keySet().size());
	// 6 imputations insérées dont 5 concernant Christophe et, dans ces 5,
	// 1 est hors du mois à considérer
	// check order
	// viessco item should be first
	Iterator<Long> iterator = results.keySet().iterator();
	// EM
	List<ImputationDto> imputations = results.get(iterator.next());
	Assert.assertEquals(1, imputations.size());
	Assert.assertEquals(IMPUT_ID_6, imputations.get(0).getImputationId());
	// VIESCO
	imputations = results.get(iterator.next());
	Assert.assertEquals(1, imputations.size());
	Assert.assertEquals(IMPUT_ID_4, imputations.get(0).getImputationId());
	Assert.assertEquals(CommonOperations.ID_CHO, imputations.get(0)
		.getResourceId());
	// LSL
	imputations = results.get(iterator.next());
	Assert.assertEquals(2, imputations.size());
	Assert.assertEquals(IMPUT_ID_1, imputations.get(0).getImputationId());
	Assert.assertEquals(IMPUT_ID_2, imputations.get(1).getImputationId());
    }

    @Test
    public void createImputation() {
	ImputationDto i = new ImputationDto(CommonOperations.ACTIVITY_LSL_ID,
		CommonOperations.ID_CHO,
		new DateTime(2014, 7, 15, 0, 0).toDate(),
		Quota.WHOLE.floatValue(), "un commentaire");

	ImputationDto created = imputationService.createImputation(i);

	Assert.assertNotNull(created.getImputationId());

	StringBuilder sql = new StringBuilder("select * from mandy.imputation")
		.append(" where date='2014-07-15'").append(" and resource_id=")
		.append(CommonOperations.ID_CHO).append(" and activity_id=")
		.append(CommonOperations.ACTIVITY_LSL_ID)
		.append(" and quota=1.0")
		.append(" and comment='un commentaire'");

	ITable table = query(sql.toString());

	Assert.assertEquals(1, table.getRowCount());
    }

    @Test
    public void updateImputation() {
	ImputationDto merge = new ImputationDto();
	merge.setImputationId(IMPUT_ID_1);
	merge.setResourceId(CommonOperations.ID_CHO);
	Date dateImputation1 = new DateTime(2014, 7, 8, 0, 0).toDate();
	merge.setDate(dateImputation1);
	merge.setQuota(Quota.QUARTER.floatValue());// modif here
	merge.setActivityId(CommonOperations.ACTIVITY_LSL_ID);
	merge.setComment("not null comment"); // and here

	imputationService.updateImputation(merge);

	ITable table = query("select * from mandy.imputation where id="
		+ IMPUT_ID_1);
	try {
	    // est-ce que la quota a bien été mis à jour ?
	    Float quota = ((Double) table.getValue(0, "quota")).floatValue();
	    Assert.assertEquals(Quota.QUARTER.floatValue(), quota);
	    // est-ce que le commentaire à bien été mis à jour ?
	    String comment = (String) table.getValue(0, "comment");
	    Assert.assertEquals("not null comment", comment);
	    // est-ce que les données existantes n'ont pas été altérées ?
	    Long resourceId = ((BigInteger) table.getValue(0, "resource_id"))
		    .longValue();
	    Assert.assertEquals(CommonOperations.ID_CHO, resourceId);
	    Long activityId = ((BigInteger) table.getValue(0, "activity_id"))
		    .longValue();
	    Assert.assertEquals(CommonOperations.ACTIVITY_LSL_ID, activityId);
	    Date date = (Date) table.getValue(0, "date");
	    Assert.assertEquals(dateImputation1, date);
	} catch (DataSetException e) {
	    Assert.fail(e.getMessage());
	    e.printStackTrace();
	}
    }

    @Test
    public void deleteImputation() {
	StringBuilder sql = new StringBuilder("insert into mandy.imputation")
		.append(" (id, activity_id, resource_id, date, quota, comment)")
		.append(" values(").append("1111,")
		.append(CommonOperations.ACTIVITY_LSL_ID).append(",")
		.append(CommonOperations.ID_CHO).append(",")
		.append("'2014-07-15',").append("1.0,").append("'foo bar')");

	execute(sql.toString());

	sql = new StringBuilder("select * from mandy.imputation where id=1111");
	ITable table = query(sql.toString());
	Assert.assertEquals(1, table.getRowCount());

	imputationService.deleteImputation(1111L);

	sql = new StringBuilder("select * from mandy.imputation where id=1111");
	table = query(sql.toString());
	Assert.assertEquals(0, table.getRowCount());

    }
}

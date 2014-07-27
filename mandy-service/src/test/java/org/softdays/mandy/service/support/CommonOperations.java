package org.softdays.mandy.service.support;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import com.ninja_squad.dbsetup.operation.Operation;

public class CommonOperations {

    public static final Long ID_LMO = 5L;
    public static final Long ID_FPI = 4L;
    public static final Long ID_FCH = 3L;
    public static final Long ID_CHO = 2L;
    public static final Long ID_RPA = 1L;
    public static final Long ACTIVITY_LSL_ID = 6L;
    public static final Long ACTIVITY_LSUN_ID = 5L;
    public static final Long ACTIVITY_TS_ID = 4L;
    public static final Long ACTIVITY_EM_ID = 3L;
    public static final Long ACTIVITY_CP_ID = 2L;
    public static final Long ACTIVITY_VIESCO_ID = 1L;
    public static final String UID_LMO = "lumorales";
    public static final String UID_FPI = "flpicollet";
    public static final String UID_FCH = "fcharbonnier1";
    public static final String UID_CHO = "chonillo";
    public static final String UID_RPA = "repatriarche";
    public static final String ACTIVITY_VIESCO_LABEL = "Vie Scolaire";
    public static final String ACTIVITY_LSL_LABEL = "LSL";
    public static final String ACTIVITY_CP_LABEL = "Congé payé";
    public static final String ACTIVITY_EM_LABEL = "Enfant malade";
    public static final String ACTIVITY_TS_LABEL = "Téléservices";
    public static final String ACTIVITY_LSUN_LABEL = "Livret Scolaire Unifié";

    public static final Operation DELETE_ALL = deleteAllFrom(
	    "MANDY.IMPUTATION", "MANDY.RESOURCE", "MANDY.ACTIVITY");

    // les activités sur lesquels on peut imputer des charges
    public static final Operation INSERT_REFERENCE_ACTIVITIES = sequenceOf(insertInto(
	    "MANDY.ACTIVITY")
	    // .withGeneratedValue("ID", ValueGenerators.sequence())

	    .columns("ID", "LABEL", "POSITION")

	    .values(ACTIVITY_VIESCO_ID, ACTIVITY_VIESCO_LABEL, 400)
	    .values(ACTIVITY_CP_ID, ACTIVITY_CP_LABEL, 201)
	    .values(ACTIVITY_EM_ID, ACTIVITY_EM_LABEL, 202)
	    .values(ACTIVITY_TS_ID, ACTIVITY_TS_LABEL, 450)
	    .values(ACTIVITY_LSUN_ID, ACTIVITY_LSUN_LABEL, 460)
	    .values(ACTIVITY_LSL_ID, ACTIVITY_LSL_LABEL, 420)

	    .build());

    // les personnes qui peuvent imputer
    public static final Operation INSERT_REFERENCE_RESOURCES = sequenceOf(insertInto(
	    "MANDY.RESOURCE")
	    // .withGeneratedValue("ID", ValueGenerators.sequence())

	    .columns("ID", "UID", "LAST_NAME", "FIRST_NAME", "ROLE")

	    .values(ID_RPA, UID_RPA, "Patriarche", "Rémi", "ROLE_ADMIN")
	    .values(ID_CHO, UID_CHO, "Onillon", "Christophe", "ROLE_USER")
	    .values(ID_FCH, UID_FCH, "Charbonnier", "François", "ROLE_MANAGER")
	    .values(ID_FPI, UID_FPI, "Picollet", "Fleur", "ROLE_USER")
	    .values(ID_LMO, UID_LMO, "Morales", "Ludovic", "ROLE_USER")

	    .build());

}

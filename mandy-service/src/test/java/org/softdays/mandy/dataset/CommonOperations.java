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

package org.softdays.mandy.dataset;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;

import org.softdays.mandy.core.model.ActivityCategory;
import org.softdays.mandy.core.model.ActivityType;
import org.softdays.mandy.core.model.Quota;

import com.ninja_squad.dbsetup.operation.Operation;

public class CommonOperations {

    public static final Long ID_TEAM_SCO = 1L;
    public static final Long ID_TEAM_GFC = 2L;
    public static final Long ID_DEL = 6L;
    public static final Long ID_LMO = 5L;
    public static final Long ID_FPI = 4L;
    public static final Long ID_FCH = 3L;
    public static final Long ID_CHO = 2L;
    public static final Long ID_RPA = 1L;
    public static final Long ACTIVITY_NO_TEAM_ID = 9L;
    public static final Long ACTIVITY_GFC_ID = 8L;
    public static final Long ACTIVITY_LSL_FIX_TEST_ID = 7L;
    public static final Long ACTIVITY_LSL_ID = 6L;
    public static final Long ACTIVITY_LSUN_ID = 5L;
    public static final Long ACTIVITY_TS_ID = 4L;
    public static final Long ACTIVITY_EM_ID = 3L;
    public static final Long ACTIVITY_CP_ID = 2L;
    public static final Long ACTIVITY_VIESCO_ID = 1L;
    public static final String UID_DEL = "deleveq";
    public static final String UID_LMO = "lumorales";
    public static final String UID_FPI = "flpicollet";
    public static final String UID_FCH = "fcharbonnier1";
    public static final String UID_CHO = "chonillo";
    public static final String UID_RPA = "repatriarche";
    public static final String ACTIVITY_VIESCO_LABEL = "Vie Scolaire";
    public static final String ACTIVITY_LSL_LABEL = "Livret Scolaire du Lycée";
    public static final String ACTIVITY_CP_LABEL = "Congé payé";
    public static final String ACTIVITY_EM_LABEL = "Enfant malade";
    public static final String ACTIVITY_TS_LABEL = "Téléservices";
    public static final String ACTIVITY_LSUN_LABEL = "Livret Scolaire Unique Numérique";

    public static final Operation DELETE_ALL = deleteAllFrom(
            "MANDY.IMPUTATION", "MANDY.TEAM_RESOURCE", "MANDY.ACTIVITY_TEAM",
            "MANDY.PREFERENCE_ACTIVITY", "MANDY.PREFERENCE", "MANDY.RESOURCE",
            "MANDY.TEAM", "MANDY.ACTIVITY");

    // les activités sur lesquels on peut imputer des charges
    public static final Operation INSERT_REFERENCE_ACTIVITIES = sequenceOf(insertInto(
            "MANDY.ACTIVITY")
            // .withGeneratedValue("ID", ValueGenerators.sequence())

            .columns("ID", "LONG_LABEL", "POSITION", "CATEGORY", "TYPE",
                    "SHORT_LABEL", "PARENT_ID")

            .values(ACTIVITY_VIESCO_ID, ACTIVITY_VIESCO_LABEL, 400,
                    ActivityCategory.PROJECT.getId(), ActivityType.UNS.getId(),
                    "VIS", null)

            .values(ACTIVITY_CP_ID, ACTIVITY_CP_LABEL, 201,
                    ActivityCategory.ABSENCE.getId(), ActivityType.UNS.getId(),
                    "CP", null)

            .values(ACTIVITY_EM_ID, ACTIVITY_EM_LABEL, 202,
                    ActivityCategory.ABSENCE.getId(), ActivityType.UNS.getId(),
                    "EM", null)

            .values(ACTIVITY_TS_ID, ACTIVITY_TS_LABEL, 450,
                    ActivityCategory.PROJECT.getId(), ActivityType.UNS.getId(),
                    "TSS", null)

            .values(ACTIVITY_LSUN_ID, ACTIVITY_LSUN_LABEL, 460,
                    ActivityCategory.PROJECT.getId(), ActivityType.UNS.getId(),
                    "LSU", null)

            .values(ACTIVITY_LSL_ID, ACTIVITY_LSL_LABEL, 420,
                    ActivityCategory.PROJECT.getId(), ActivityType.UNS.getId(),
                    "LSL", null)

            .values(ACTIVITY_LSL_FIX_TEST_ID, "My long label for mantis 11111",
                    430, ActivityCategory.PROJECT.getId(),
                    ActivityType.FIX.getId(), "MANTIS#11111", ACTIVITY_LSL_ID)

            .values(ACTIVITY_GFC_ID, "Gestion Financière et Comptable", 440,
                    ActivityCategory.PROJECT.getId(), ActivityType.UNS.getId(),
                    "GFC", null)

            .values(ACTIVITY_NO_TEAM_ID, "Activity not yet related to a team",
                    500, ActivityCategory.PROJECT.getId(),
                    ActivityType.UNS.getId(), "NO_TEAM_ACTIVITY", null)

            .build());

    // les équipes sur lesquelles on peut affecter des ressources
    public static final Operation INSERT_REFERENCE_TEAM = sequenceOf(insertInto(
            "MANDY.TEAM")
            // .withGeneratedValue("ID", ValueGenerators.sequence())

            .columns("ID", "CODE", "LABEL")

            .values(ID_TEAM_SCO, "DEVNAT-SCO", "Equipe Dev Nat Scolarité")
            .values(ID_TEAM_GFC, "DEVNAT-GFC", "Equipe Dev Nat GFC")

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
            .values(ID_DEL, UID_DEL, "Eleveq", "David", "ROLE_USER")

            .build());

    // les préférences des personnes
    public static final Operation INSERT_PREFERENCES = sequenceOf(insertInto(
            "MANDY.PREFERENCE")

    .columns("RESOURCE_ID", "GRANULARITY")

    .values(ID_CHO, Quota.HALF.floatValue())
            .values(ID_LMO, Quota.QUARTER.floatValue()).build());

    // association personne-équipe
    public static final Operation INSERT_REFERENCE_TEAM_RESOURCE = sequenceOf(insertInto(
            "MANDY.TEAM_RESOURCE")
    // .withGeneratedValue("ID", ValueGenerators.sequence())

            .columns("RESOURCE_ID", "TEAM_ID")

            .values(ID_RPA, ID_TEAM_SCO)

            .values(ID_RPA, ID_TEAM_GFC)

            .values(ID_CHO, ID_TEAM_SCO)

            .values(ID_FCH, ID_TEAM_SCO)

            .values(ID_FPI, ID_TEAM_SCO)

            .values(ID_LMO, ID_TEAM_SCO)

            .values(ID_DEL, ID_TEAM_GFC)

            .build());

    // association équipe-activités
    public static final Operation INSERT_REFERENCE_ACTIVITY_TEAM = sequenceOf(insertInto(
            "MANDY.ACTIVITY_TEAM")
    // columns
            .columns("ACTIVITY_ID", "TEAM_ID")
            // values
            .values(ACTIVITY_LSL_ID, ID_TEAM_SCO)

            .values(ACTIVITY_LSL_FIX_TEST_ID, ID_TEAM_SCO)

            .values(ACTIVITY_LSUN_ID, ID_TEAM_SCO)

            .values(ACTIVITY_TS_ID, ID_TEAM_SCO)

            .values(ACTIVITY_VIESCO_ID, ID_TEAM_SCO)

            .values(ACTIVITY_GFC_ID, ID_TEAM_GFC)

            .build());

    public static final Operation INSERT_PREFERENCES_ACTIVITIES = sequenceOf(insertInto(
            "MANDY.PREFERENCE_ACTIVITY")
    // columns
            .columns("PREFERENCE_ID", "ACTIVITY_ID", "ACTIVITY_ORDER")
            // values
            .values(ID_CHO, ACTIVITY_VIESCO_ID, 0)

            .values(ID_CHO, ACTIVITY_LSUN_ID, 1)

            .values(ID_LMO, ACTIVITY_LSL_ID, 0)

            .build());

}

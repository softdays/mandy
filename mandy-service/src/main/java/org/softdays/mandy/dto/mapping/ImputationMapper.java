package org.softdays.mandy.dto.mapping;

import org.softdays.mandy.core.model.Imputation;
import org.softdays.mandy.dto.ImputationDto;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = IoC.SPRING,
        withCustomFields = { @Field({ "Imputation.id", "imputationId" }),
                @Field({ "Imputation.activity.id", "activityId" }),
                @Field({ "Imputation.resource.id", "resourceId" }) })
public interface ImputationMapper {

    ImputationDto map(Imputation source);

    Imputation map(ImputationDto source);
}

package org.softdays.alokate.dto;

import org.softdays.alokate.model.Imputation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rpatriarche on 02/03/14.
 */
@Component
public class DtoFactory {

    public List<ImputationDto> convert(Collection<Imputation> imputations) {
        List<ImputationDto> results = new ArrayList<ImputationDto>(imputations.size());
        for (Imputation imputation : imputations) {
            ImputationDto dto = new ImputationDto();
            BeanUtils.copyProperties(imputation, dto);
            results.add(dto);
        }
        return results;
    }
}

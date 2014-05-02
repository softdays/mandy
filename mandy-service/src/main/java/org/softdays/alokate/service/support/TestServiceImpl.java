package org.softdays.alokate.service.support;

import org.softdays.alokate.dto.TestDto;
import org.softdays.alokate.service.TestService;
import org.springframework.stereotype.Service;


/**
 * Created by rpatriarche on 01/03/14.
 */
@Service
public class TestServiceImpl implements TestService {

    public TestDto findById(Integer id) {
        return new TestDto(id, "test");
    }
}

package org.softdays.alokate.service;

import org.softdays.alokate.dto.TestDto;

/**
 * Created by rpatriarche on 01/03/14.
 */
public interface TestService {

    TestDto findById(Integer id);
}

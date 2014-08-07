package org.softdays.mandy.dao;

import org.softdays.mandy.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository.
 */
public interface ResourceDao extends JpaRepository<Resource, Long> {

    Resource findOneByUid(String uid);

}

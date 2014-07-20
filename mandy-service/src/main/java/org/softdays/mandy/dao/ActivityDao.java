package org.softdays.mandy.dao;

import org.softdays.mandy.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository.
 */
public interface ActivityDao extends JpaRepository<Activity, Integer> {
}

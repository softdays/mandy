package org.softdays.mandy.dao;

import java.util.List;

import org.softdays.mandy.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository.
 */
public interface ActivityDao extends JpaRepository<Activity, Integer> {

    @Query("select distinct a from #{#entityName} a left join fetch a.teams t"
	    + " where :resourceId in elements(t.resources)"
	    + " or a.type <> org.softdays.mandy.model.ActivityType.P"
	    + " order by a.type asc, a.position asc")
    List<Activity> findByResource(@Param("resourceId") Long userId);

}

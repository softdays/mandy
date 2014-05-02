package org.softdays.alokate.dao;

import org.softdays.alokate.model.Activity;

import java.util.List;

/**
 * Created by rpatriarche on 08/03/14.
 */
public interface ActivityDao {

    List<Activity> findAll();
}

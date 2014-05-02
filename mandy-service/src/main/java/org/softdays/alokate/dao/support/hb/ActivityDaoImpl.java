package org.softdays.alokate.dao.support.hb;

import org.softdays.alokate.dao.ActivityDao;
import org.softdays.alokate.model.Activity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by rpatriarche on 08/03/14.
 */
@Repository
public class ActivityDaoImpl implements ActivityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Activity> findAll() {
        return entityManager.createQuery("from Activity order by position asc", Activity.class).getResultList();
    }
}

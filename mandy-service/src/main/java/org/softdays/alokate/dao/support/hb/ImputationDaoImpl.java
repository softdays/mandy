package org.softdays.alokate.dao.support.hb;

import org.softdays.alokate.dao.ImputationDao;
import org.softdays.alokate.model.Imputation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by rpatriarche on 02/03/14.
 */
@Repository
public class ImputationDaoImpl implements ImputationDao {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Imputation> findImputations(Integer userId, Date date) {
        return null;
    }
}

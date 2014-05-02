package org.softdays.alokate.dao;

import org.softdays.alokate.model.Imputation;

import java.util.Date;
import java.util.List;

/**
 * Created by rpatriarche on 02/03/14.
 */
public interface ImputationDao {

    /**
     * Retourne l'ensemble des imputations retrouvées pour l'utilisateur
     * et pour le mois correspondant à la date indiquée.
     * @param userId
     * @param date La date du mois pour lequel on souhaite récupérer les imputations.
     * @return Une liste d'entités @{link Imputation}.
     */
    List<Imputation> findImputations(Integer userId, Date date);
}

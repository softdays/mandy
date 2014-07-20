package org.softdays.mandy.dao;

import java.util.Date;
import java.util.List;

import org.softdays.mandy.model.Imputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository.
 */
public interface ImputationDao extends JpaRepository<Imputation, Integer> {

    /**
     * Retourne l'ensemble des imputations retrouvées pour l'utilisateur et pour
     * le mois correspondant à la date indiquée.
     * 
     * @param userId
     *            Identifiant utilisateur.
     * @param date
     *            La date du mois pour lequel on souhaite récupérer les
     *            imputations.
     * @return Une liste d'entités @{link Imputation}.
     */
    @Query("select i from #{#entityName} i join fetch i.resource r join fetch i.activity a where r.id=:resourceId and (i.date between :startDate and :endDate) order by a.position asc, i.date asc")
    // version using just method name but without fetch:
    // findByResourceIdAndDateBetweenOrderByActivityPositionAscDateAsc
    List<Imputation> findByResourceAndDateRange(
	    @Param("resourceId") Long userId,
	    @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

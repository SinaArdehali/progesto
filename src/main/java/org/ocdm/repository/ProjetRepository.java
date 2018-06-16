package org.ocdm.repository;

import org.ocdm.domain.Projet;
import org.ocdm.domain.Tache;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Projet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    @Query("select distinct projet from Projet projet left join fetch projet.membresProjets left join fetch projet.chefDuProjets")
    List<Projet> findAllWithEagerRelationships();

    @Query("select projet from Projet projet left join fetch projet.membresProjets left join fetch projet.chefDuProjets where projet.id =:id")
    Projet findOneWithEagerRelationships(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Projet projet set projet.joursVendusProjet = (select sum(tache.joursVendusTache) from Tache tache where tache.projetMere.id =:id) where projet.id =:id")
    int majJoursVendProj(@Param("id") Long id);
}


//    COMMENTAIRES A SUPPRIMER
//    @Query("UPDATE Projet projet set joursVendusProjet = (select sum(joursVendusTache) from Tache tache where tache.projetMere.id =:id) where projet.id =:id")
//    Projet majJoursVendProj(@Param("id") Long id);
//    version brouillon
//    @Query("UPDATE projet set joursVendusProjet = (select sum(joursVendusTache) from Tache tache where tache.projetMere.id =:tache.projet_mere_id) where projet.id =:tache.projet_mere_id")
//    Void majJoursVendProj(@Param("tache") Tache tache);
//    cree par moiSina
//@Modifying
//@Transactional
//    @Query("UPDATE Projet projet set jours_vendus_projet = (select sum(jours_vendus_tache) from Tache tache where tache.projet_mere_id =:id) where projet.id =:id")
//    Projet majJoursVendProj(@Param("id") Long id);



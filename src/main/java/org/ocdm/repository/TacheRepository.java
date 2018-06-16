package org.ocdm.repository;

import org.ocdm.domain.AttributionTache;
import org.ocdm.domain.Projet;
import org.ocdm.domain.Tache;
import org.ocdm.service.dto.TacheEtDonneesDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Tache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {
    @Query("select distinct tache from Tache tache where tache.projetMere.id =:id order by tache.nomTache asc")
    List<Tache> getAllByProjId(@Param("id") Long id);









//    @Query(value="select att.tache_mere_id from attribution_tache att\n" +
//        "    left join jhi_user as us on att.proprietaire_tache_id = us.id\n" +
//        "    where us.login = ?1\n" +
//        "    group by att.tache_mere_id", nativeQuery = true)
//    List<Tache> getTachesCurrentUser(@Param("login") String login);







    @Query(value="select count(id) from tache where tache.nom_tache = ?1 and tache.projet_mere_id = ?2", nativeQuery = true)
    Float existeDeja(@Param("nomTache") String nomTache, @Param("idProjetMere") Long idProjetMere);



    @Query("select new org.ocdm.service.dto.TacheEtDonneesDTO(tache.id, tache.nomTache, tache.joursVendusTache, count(tache.id)) from Tache tache where tache.projetMere.id =:idProjet group by tache.id, tache.nomTache, tache.joursVendusTache order by tache.nomTache asc")
    List<TacheEtDonneesDTO> getAllByProjIdBIS(@Param("idProjet") Long idProjet);

    @Query("select new org.ocdm.service.dto.TacheEtDonneesDTO(attributionTache.tacheMere.id, attributionTache.tacheMere.nomTache, attributionTache.tacheMere.joursVendusTache, count(attributionTache.id)) from AttributionTache attributionTache where (attributionTache.tacheMere.projetMere.id=:idProjet and attributionTache.proprietaireTache is not null) group by attributionTache.tacheMere.id, attributionTache.tacheMere.nomTache, attributionTache.tacheMere.joursVendusTache order by attributionTache.tacheMere.nomTache asc" )
    List<TacheEtDonneesDTO> getJoursAttrByTache(@Param("idProjet") Long idProjet);


    @Query("select new org.ocdm.service.dto.TacheEtDonneesDTO(attributionTache.tacheMere.id, attributionTache.tacheMere.nomTache, attributionTache.tacheMere.joursVendusTache, count(attributionTache.id)) from AttributionTache attributionTache where (attributionTache.tacheMere.projetMere.id=:idProjet and attributionTache.proprietaireTache is null) group by attributionTache.tacheMere.id, attributionTache.tacheMere.nomTache, attributionTache.tacheMere.joursVendusTache order by attributionTache.tacheMere.nomTache asc" )
    List<TacheEtDonneesDTO> getJoursNonAttrByTache(@Param("idProjet") Long idProjet);
}

//COMMENTAIRES A SUPPRIMER
/**
 * Spring Data JPA repository for the Tache entity.
 */
//@SuppressWarnings("unused")
//@Repository
//public interface TacheRepository extends JpaRepository<Tache, Long> {
//    @Query("select tache from Tache tache where tache.projetMere =:projet")
//    List<Tache> findAllByProjet(@Param("projet") Projet projet);
//}

//    @Query("UPDATE projet set jours_vendus_projet = (select sum(jours_vendus_tache) from Tache tache where tache.projet_mere_id =:tache.projet_mere_id) where projet.id =:tache.projet_mere_id")
//    void majJoursVendProj(@Param("tache") Tache tache);

//    cree par moiSina
//@Modifying
//@Transactional
//    @Query("UPDATE Projet projet set jours_vendus_projet = (select sum(jours_vendus_tache) from Tache tache where tache.projet_mere_id =:id) where projet.id =:id")
//    Projet majJoursVendProj(@Param("id") Long id);


// JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR LE BLOC CI DESSOUS CORRESPOND A LA LIGNE TOTAL QUE J'AI INSERE ET QUI SE DEPLOYAIT QUAND ON CLIQUAIT SUR "VOIR LES TACHES DU PROJET"
//    @Query("select sum(tache.joursVendusTache) from Tache tache where tache.projetMere.id =:id")
//    Float trouverSumJoursVendus(@Param("id") Long id);

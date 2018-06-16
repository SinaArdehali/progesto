package org.ocdm.repository;

import org.ocdm.domain.AttributionTache;
import org.ocdm.domain.Tache;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the AttributionTache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributionTacheRepository extends JpaRepository<AttributionTache, Long> {

    @Query("select attribution_tache from AttributionTache attribution_tache where attribution_tache.proprietaireTache.login = ?#{principal.username}")
    List<AttributionTache> findByProprietaireTacheIsCurrentUser();

//    //select attributionTache from AttributionTache attributionTache where attributionTache.proprietaireTache.id is null and attributionTache.tacheMere.id =:idTache
//    @Query("select attributionTache from AttributionTache attributionTache\n" +
//        "    left join User user on attributionTache.proprietaireTache = user\n" +
//        "    where user.login =:login\n" +
//        "    group by attributionTache.tacheMere, attributionTache.id")
//    List<AttributionTache> getTachesCurrentUser(@Param("login") String login);


    @Query("select distinct tache from Tache tache" +
        "    left join AttributionTache attributionTache on attributionTache.tacheMere = tache\n" +
        "    left join User user on attributionTache.proprietaireTache = user\n" +
        "    where user.login =:login\n")
    List<Tache> getTachesCurrentUser(@Param("login") String login);





    @Query("select count(attributionTache.id) from AttributionTache attributionTache where attributionTache.proprietaireTache.id =:idInge and attributionTache.tacheMere.id =:idTache")
    Float getNbJoursAttrib(@Param("idInge") Long idInge, @Param("idTache") Long idTache);

    @Query(value="WITH cte AS (select * from attribution_tache a WHERE a.tache_mere_id = ?1 AND a.proprietaire_tache_id is null limit 2) UPDATE attribution_tache a SET proprietaire_tache_id = '5' FROM cte WHERE a.id = cte.id", nativeQuery = true)
    int getFirstNRows(Long idTache);

    @Query(value="select count(id) from attribution_tache where proprietaire_tache_id = ?1 and jhi_date >= current_date and jhi_date <= current_date + interval '1 month' * 1", nativeQuery = true)
    Float nbJoursDispM(@Param("idInge") Long idInge);

    @Query(value="select count(id) from attribution_tache where proprietaire_tache_id = ?1 and jhi_date >= current_date and jhi_date <= current_date + interval '2 month' * 1", nativeQuery = true)
    Float nbJoursDispM1(@Param("idInge") Long idInge);

    @Query(value="select count(id) from attribution_tache where proprietaire_tache_id = ?1 and jhi_date >= current_date and jhi_date <= current_date + interval '3 month' * 1", nativeQuery = true)
    Float nbJoursDispM2(@Param("idInge") Long idInge);

    @Query("select attributionTache from AttributionTache attributionTache where attributionTache.proprietaireTache.id is null and attributionTache.tacheMere.id =:idTache")
    List<AttributionTache> getAttrTacheLimit(@Param("idTache") Long idTache, Pageable pageable);

    @Modifying
    @Query(value="UPDATE attribution_tache SET proprietaire_tache_id = ?1 where id in ?2", nativeQuery = true)
    void updateResultSelectLimit(Long idInge, Set<Long> setIdResultSelectLimit);

    @Modifying
    @Query(value="UPDATE attribution_tache SET jhi_date = ?3 where proprietaire_tache_id = ?1 and tache_mere_id = ?2 and jhi_date is null", nativeQuery = true)
    void setDateAttrib(Long idInge, Long idTache, LocalDate dateAinserer);

    @Query(value="select count(id) from attribution_tache where proprietaire_tache_id is null and tache_mere_id = ?1", nativeQuery = true)
    Float getMaxJours(@Param("idTache") Long idTache);

//COMMENTAIRES A SUPPRIMER
//    sauvegarde ma requete pour getNbJoursAZttribues:
//    @Query("select count(*) from AttributionTache attributionTache where attributionTache.proprietaireTache.id =:idInge and attributionTache.tacheMere.id =: idTache")

//    requete ci dessous fonctiçonne mais il nyu a pas de limite
//    SELECT tache_mere_id,proprietaire_tache_id FROM attribution_tache
//    WHERE attribution_tache.tache_mere_id = '2101'
//    and attribution_tache.proprietaire_tache_id is null
//    ORDER BY attribution_tache.tache_mere_id LIMIT 2
//    @Query("select attributionTache from AttributionTache attributionTache where attributionTache.tacheMere.id =:idTache and attributionTache.proprietaireTache.id is null order by attributionTache.tacheMere.id ")
//    List<AttributionTache> getFirstNRows(@Param("idTache") Long idTache);

//    en sql
//        SELECT tache_mere_id,proprietaire_tache_id FROM attribution_tache
//    WHERE attribution_tache.tache_mere_id = '2101'
//    and attribution_tache.proprietaire_tache_id is null
//    ORDER BY attribution_tache.tache_mere_id LIMIT 2
//    @Query(value="SELECT tache_mere_id,proprietaire_tache_id FROM attribution_tache WHERE attribution_tache.tache_mere_id = ?1 and attribution_tache.proprietaire_tache_id is null ORDER BY attribution_tache.tache_mere_id LIMIT 2", nativeQuery = true)
//    List<AttributionTache> getFirstNRows(Long idTache);

//    @Query(value="SELECT * FROM attribution_tache WHERE attribution_tache.tache_mere_id = ?1 and attribution_tache.proprietaire_tache_id is null ORDER BY attribution_tache.tache_mere_id LIMIT 2", nativeQuery = true)
//    List<AttributionTache> getFirstNRows(Long idTache);

//    @Query("update AttributionTache attributionTache SET attributionTache.proprietaireTache.id =:idInge where attributionTache.id in : setIdResultSelectLimit")
//    int updateResultSelectLimit(@Param("idInge") Long idInge, @Param("setIdResultSelectLimit")Set<Long> setIdResultSelectLimit);

//
//    @Query(value="INSERT into attribution_tache(tache_mere_id) values ('?1')", nativeQuery = true)
//    void modifierJoursVendus(@Param("idTache") Long idTache);


//    @Query("select count(attributionTache.id) from AttributionTache attributionTache where attributionTache.proprietaireTache.id is null and attributionTache.tacheMere.id =:idTache")
//    Float getMaxJours(@Param("idTache") Long idTache);




}






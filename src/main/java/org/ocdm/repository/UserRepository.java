package org.ocdm.repository;

import org.ocdm.domain.Projet;
import org.ocdm.domain.User;
import org.ocdm.service.dto.IngeSansAttribDTO;
import org.ocdm.service.dto.ParticTacheDTO;
import org.ocdm.service.dto.EmplMDTO;
import org.ocdm.service.dto.TacheEtDonneesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    @Query("select new org.ocdm.service.dto.ParticTacheDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.tacheMere.id=:id group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
    List<ParticTacheDTO> getParticByTache(@Param("id") Long id);

    @Query("select distinct user from User user where user.id not in (1,2,3,4)")
    List<User> queryPersonnesNonAffectees();

    @Query(value="select * from jhi_user FULL JOIN jhi_user_authority ON jhi_user.id = jhi_user_authority.user_id where jhi_user_authority.user_id not in ('1','3','4') and jhi_user_authority.authority_name in ('ROLE_USER')", nativeQuery = true)
    List<User> getAllInge();


    @Query(value="select count(id) from attribution_tache where proprietaire_tache_id = ?1 and jhi_date >= current_date and jhi_date <= current_date + interval '1 month' * 1", nativeQuery = true)
    Float nbJoursDispM(@Param("idInge") Long idInge);


    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourRestantUntilFinDuMois-(count(attributionTache.date)/4.0F)) from AttributionTache attributionTache where (attributionTache.date <= :dernierJourMoisM and attributionTache.date >= :premierJourMoisM) or attributionTache.date is null group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login order by attributionTache.proprietaireTache.login asc" )
    List<EmplMDTO> getAllIngeM(@Param("nombreDeJourRestantUntilFinDuMois") Float nombreDeJourRestantUntilFinDuMois, @Param("premierJourMoisM") LocalDate premierJourMoisM, @Param("dernierJourMoisM") LocalDate dernierJourMoisM);

    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourDuMoisPlus1-(count(attributionTache.date)/4.0F)) from AttributionTache attributionTache where (attributionTache.date <= :dernierJourMoisMplus1 and attributionTache.date >= :premierJourMoisMplus1) or attributionTache.date is null group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login order by attributionTache.proprietaireTache.login asc" )
    List<EmplMDTO> getAllIngeM1(@Param("nombreDeJourDuMoisPlus1") Float nombreDeJourDuMoisPlus1, @Param("premierJourMoisMplus1") LocalDate premierJourMoisMplus1, @Param("dernierJourMoisMplus1") LocalDate dernierJourMoisMplus1);

    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourDuMoisPlus2-(count(attributionTache.date)/4.0F)) from AttributionTache attributionTache where (attributionTache.date <= :dernierJourMoisMplus2 and attributionTache.date >= :premierJourMoisMplus2) or attributionTache.date is null group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login order by attributionTache.proprietaireTache.login asc" )
    List<EmplMDTO> getAllIngeM2(@Param("nombreDeJourDuMoisPlus2") Float nombreDeJourDuMoisPlus2, @Param("premierJourMoisMplus2") LocalDate premierJourMoisMplus2, @Param("dernierJourMoisMplus2") LocalDate dernierJourMoisMplus2);

    @Query(value="select login \n" +
        "from jhi_user\n" +
        "\n" +
        "join jhi_user_authority\n" +
        "on jhi_user.id = jhi_user_authority.user_id\n" +
        "\n" +
        "where jhi_user_authority.authority_name='ROLE_USER'\n" +
        "and jhi_user_authority.authority_name <> 'ROLE_ADMIN'\n" +
        "and jhi_user.login not in ('system', 'admin', 'user')\n" +
        "and not exists (select proprietaire_tache_id from attribution_tache where attribution_tache.proprietaire_tache_id = jhi_user.id)", nativeQuery = true)
    List<String> getAllIngeNoAttrib();

    @Query("select user.id from User user where user.login=:login")
    Long getIdFromLogin(@Param("login") String login);

}



//COMMENTAIRES A SUPPRIMER

//la requete ci dessous fonctionne, mais j'essaie de recuperer egaelemnt le count'
//    @Query("select distinct attributionTache.proprietaireTache from AttributionTache attributionTache where attributionTache.tacheMere.id =:id")
//    List<User> getParticByTache(@Param("id") Long id);


//sauvegarde de ma premiere ebauche de requete:
//peut etre qu'il faut mettre tout ca sous forme de List<User>
//@Query("select distinct attributionTache.proprietaireTache, count(attributionTache.id) from AttributionTache attributionTache where attributionTache.tacheMere.id=:id group by attributionTache.proprietaireTache.id order by attributionTache.proprietaireTache.id asc")
//List<ParticTacheDTO> getParticByTache(@Param("id") Long id);



//    @Query("select distinct attributionTache.proprietaireTache.id , count(attributionTache.id) from AttributionTache attributionTache join attributionTache.proprietaireTache ap where attributionTache.tacheMere.id=:id group by ap.id")


//    @Query("select distinct attributionTache.id, count(attributionTache.id) from AttributionTache attributionTache join attributionTache.proprietaireTache pT where attributionTache.tacheMere.id=:id group by pT.id order by pT.id asc")
//    attributionTache.proprietaireTache
//
//    List<ParticTacheDTO> getParticByTache(@Param("id") Long id);
//
//je dois faire 2 requetes
//    une requete qui recupere une map avec les ids et les resultats de mon
//et une autre requete qui recupere l'objet user en fonction de l'id
//

//    @Query("select distinct attributionTache.proprietaireTache.id , count(attributionTache.id) from AttributionTache attributionTache join attributionTache.proprietaireTache ap where attributionTache.tacheMere.id=:id group by ap.id")
//    @Query("select new org.ocdm.service.dto.ParticTacheDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.tacheMere.id=:id group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//    List<ParticTacheDTO> queryPersonnesNonAffectees(@Param("id") Long id);

//    @Query("select new org.ocdm.service.dto.ParticTacheDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.tacheMere.id=:id group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//    List<ParticTacheDTO> queryPersonnesNonAffectees(@Param("id") Long id);

//    @Query("select a from Resultat a where a.id in (select new org.ocdm.service.dto.ParticTacheDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.tacheMere.id=:id group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login)")
//    List<ParticTacheDTO> queryPersonnesNonAffectees(@Param("id") Long id);

//    @Query("select distinct projet from Projet projet left join fetch projet.membresProjets left join fetch projet.chefDuProjets")
//    List<Projet> findAllWithEagerRelationships();
//
//    @Query("select user User from User where ")
//    List<User> findAllInge();


//    @Query("select distinct attributionTache.proprietaireTache.id , count(attributionTache.id) from AttributionTache attributionTache join attributionTache.proprietaireTache ap where attributionTache.tacheMere.id=:id group by ap.id")


//    from jhi_user
//    FULL JOIN jhi_user_authority ON jhi_user.id = jhi_user_authority.user_id
//    where jhi_user_authority.user_id not in (1,2,3,4)

//    @Query(value="select proprietaire_tache_id, count(id) from attribution_tache where jhi_date >= current_date and jhi_date <= current_date + interval '1 month' * 1 group by attribution_tache.proprietaire_tache_id", nativeQuery = true)
//    List<EmplMDTO> getAllIngeM();


//    @Query(value="select proprietaire_tache_id, count(id) from attribution_tache where jhi_date >= current_date and jhi_date <= current_date + interval '1 month' * 1 group by attribution_tache.proprietaire_tache_id", nativeQuery = true)
//    @Query("select new org.ocdm.service.dto.ParticTacheDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.tacheMere.id=:id group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")

//    ci dessous, on voit la requete SQL qui fonctionne:
//    @Query(value="select proprietaire_tache_id, count(id) from attribution_tache where jhi_date >= current_date and jhi_date <= current_date + interval '1 month' * 1 group by attribution_tache.proprietaire_tache_id", nativeQuery = true)




//    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.date >= current_date group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//    List<EmplMDTO> getAllIngeM1();
//
//    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.date >= current_date group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//    List<EmplMDTO> getAllIngeM2();





//between current_date and (current_date + 1 month) group by

//    a montrer à Tommy:
//   @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.date >= current_date and attributionTache.date <= (current_date + 30 Days) group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//    List<EmplMDTO> getAllIngeM();



//    la requete JPA ci dessous fonctionne,  mais elle ne correspond pas a ce que je veux
//   @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourRestantUntilFinDuMois-count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.date >= current_date group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//  List<EmplMDTO> getAllIngeM(@Param("nombreDeJourRestantUntilFinDuMois") Long nombreDeJourRestantUntilFinDuMois);
//List<EmplMDTO> getAllIngeM(@Param("nbJoursFinDuMois") Long nbJoursFinDuMois);

//    ou bien je transforme la requete ci dessous en quelque chose qui balance le bigInt dans un objet EmplMDTO
//        @Query(value="select proprietaire_tache_id, count(id) from attribution_tache where jhi_date >= current_date and jhi_date <= current_date + interval '1 month' * 1 group by attribution_tache.proprietaire_tache_id", nativeQuery = true)
//        List<EmplMDTO> getAllIngeM();


//    voici mon brouillon
//    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, 30-count(attributionTache.proprietaireTache.id) where ) from AttributionTache attributionTache where attributionTache.date >= current_date group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login")
//    List<EmplMDTO> getAllIngeM();

//    @Query(value="select :nombreDeJourDuMoisProchain-count(attribution_tache.proprietaire_tache_id) from attribution_tache where jhi_date < :dateFinMoisProchain and jhi_date > :dateDebutMoisProchain group by proprietaire_tache_id", nativeQuery = true)
//    List<Float> getAllIngeM1(@Param("nombreDeJourDuMoisProchain") Long nombreDeJourDuMoisProchain, @Param("dateDebutMoisProchain") java.util.Date dateDebutMoisProchain, @Param("dateFinMoisProchain") java.util.Date dateFinMoisProchain);

//    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourRestantUntilFinDuMois-count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login" )
//    List<EmplMDTO> getAllIngeM(@Param("nombreDeJourRestantUntilFinDuMois") Long nombreDeJourRestantUntilFinDuMois);

//    ci dessous version qui fonctionne , mais il me faut tous els ignenieurs
//    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourRestantUntilFinDuMois-count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.date < :dernierJourMoisM and attributionTache.date > :premierJourMoisM group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login" )
//    List<EmplMDTO> getAllIngeM(@Param("nombreDeJourRestantUntilFinDuMois") Long nombreDeJourRestantUntilFinDuMois, @Param("premierJourMoisM") LocalDate premierJourMoisM, @Param("dernierJourMoisM") LocalDate dernierJourMoisM);

//    @Query("select new org.ocdm.service.dto.EmplMDTO(attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login, :nombreDeJourRestantUntilFinDuMoisplus1-count(attributionTache.proprietaireTache.id)) from AttributionTache attributionTache where attributionTache.date < :dernierJourMoisMplus1 and attributionTache.date > :premierJourMoisMplus1 group by attributionTache.proprietaireTache.id, attributionTache.proprietaireTache.login" )
//    List<EmplMDTO> getAllIngeM1(@Param("nombreDeJourRestantUntilFinDuMoisplus1") Long nombreDeJourRestantUntilFinDuMoisplus1, @Param("premierJourMoisMplus1") LocalDate premierJourMoisMplus1, @Param("dernierJourMoisMplus1") LocalDate dernierJourMoisMplus1);

//    verison que je suis ent rain d'ameliorer'


//    @Query(value="\n" +
//        "select login \n" +
//        "from jhi_user\n" +
//        "\n" +
//        "join jhi_user_authority\n" +
//        "on jhi_user.id = jhi_user_authority.user_id\n" +
//        "\n" +
//        "where jhi_user_authority.authority_name='ROLE_USER'\n" +
//        "and jhi_user_authority.authority_name <> 'ROLE_ADMIN'\n" +
//        "and jhi_user.login not in ('system', 'admin', 'user')\n" +
//        "and jhi_user.id NOT IN (SELECT proprietaire_tache_id FROM attribution_tache)", nativeQuery = true)
//    List<String> getAllIngeNoAttrib();


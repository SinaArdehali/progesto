package org.ocdm.service;

import org.ocdm.domain.AttributionTache;
import org.ocdm.domain.Projet;
import org.ocdm.domain.Tache;
import org.ocdm.service.dto.TacheEtDonneesDTO;
import org.ocdm.service.dto.TacheEtDonneesDTOComplet;

import java.util.List;

/**
 * Service Interface for managing Tache.
 */
public interface TacheService {

    /**
     * Save a tache.
     *
     * @param tache the entity to save
     * @return the persisted entity
     */
    Tache save(Tache tache);


    /**
     *  Get all the taches.
     *
     *  @return the list of entities
     */
    List<Tache> findAll();

//    /**
//     *  Get all the taches by project.
//     *
//     *  @return the list of entities
//     */
//    List<Tache> findAllByProjet(Projet projet);

    /**
     *  Get all the taches by project id.
     *
     *  @return the list of entities
     */
    List<Tache> getAllByProjId(Long id);



    /**
     *  Get all the taches by project id.
     *
     *  @return the list of entities
     */
    List<Tache> getTachesCurrentUser(String login);


    /**
     *  Get the "id" tache.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Tache findOne(Long id);

    /**
     *  Delete the "id" tache.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);


    // JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR A PRESENT LA MAJ DU PROJET EN MEME TEMPS QUE LA MAJ DE LA TACHE'
    /**
     * maj projet.
     *
     * @param tache the entity to recuperer projetMere from
     * @return the persisted entity
     */
    int majJoursVendProj(Tache tache);

//    // JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR LE BLOC CI DESSOUS CORRESPOND A LA LIGNE TOTAL QUE J'AI INSERE ET QUI SE DEPLOYAIT QUAND ON CLIQUAIT SUR "VOIR LES TACHES DU PROJET"
//    /**
//     *  Get the "id" projet.
//     *
//     *  @param id the id of the projet to retrieve sum of.
//     *  @return float
//     */
//    float trouverSumJoursVendus(Long id);




//en balancant la tache en entier
//    /**
//     * maj projet.
//     *
//     * @param tacheMere the entity to save
//     * @param nbJour the entity to save
//     * @return the persisted entity
//     */
//    int modifierJoursVendus(long nbJour, Tache tacheMere);*


    /**
          * maj projet.
          *
          * @param idTacheMere the entity to save
          * @param nbJour the entity to save
          * @return the persisted entity
          */
    List<AttributionTache> modifierJoursVendus(long nbJour, long idTacheMere);



    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *  @param nbJour the id of the user to retrieve count of all the jours attribues
     *  @param idTacheMere the id of the Tache
     *
     *  @return List<AttributionTache>
     */
   float selectPuisRetirerJoursVendus(Double nbJour, Long idTacheMere);









    /**
     *  Get all the taches by project id.
     *
     * @param idProjet the id of the user to retrieve count of all the jours attribues
     *
     *  @return the list of entities
     */
    List<TacheEtDonneesDTOComplet> getAllByProjIdBIS(Long idProjet);



//
//    /**
//     *  Get all the taches and their data from an "id" project.
//     *
//     *  @param idProjet the id of the user to retrieve count of all the jours attribues
//     *
//     *  @return List<TacheEtDonneesDTOComplet>
//     */
//    public List<TacheEtDonneesDTOComplet> getJoursAttrByTache(Long idProjet);


}

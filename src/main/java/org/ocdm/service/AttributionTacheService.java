package org.ocdm.service;

import org.ocdm.domain.AttributionTache;
import java.util.List;

/**
 * Service Interface for managing AttributionTache.
 */
public interface AttributionTacheService {

    /**
     * Save a attributionTache.
     *
     * @param attributionTache the entity to save
     * @return the persisted entity
     */
    AttributionTache save(AttributionTache attributionTache);

    /**
     *  Get all the attributionTaches.
     *
     *  @return the list of entities
     */
    List<AttributionTache> findAll();

    /**
     *  Get the "id" attributionTache.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AttributionTache findOne(Long id);

    /**
     *  Delete the "id" attributionTache.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);



    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *  @param idInge the id of the user to retrieve count of all the jours attribues
     *  @param idTache the id of the tache mere
     *
     *  @return float
     */
    float getNbJoursAttrib(Long idInge, Long idTache);



    /**
     * Save a attributionTache.
     *
     * @param idTache the entity to save
     * @return the persisted entity
     */
    int getFirstNRows(Long idTache);



    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *  @param idInge the id of the user to retrieve count of all the jours attribues
     *  @param idInge the id of the User
     *
     *  @return float
     */
    float nbJoursDispM(Long idInge);

    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *  @param idInge the id of the user to retrieve count of all the jours attribues
     *  @param idInge the id of the User
     *
     *  @return float
     */
    float nbJoursDispM1(Long idInge);

    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *  @param idInge the id of the user to retrieve count of all the jours attribues
     *  @param idInge the id of the User
     *
     *  @return float
     */
    float nbJoursDispM2(Long idInge);


    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *  @param idTache the id of the user to retrieve count of all the jours attribues
     *  @param idTache the id of the Tache
     *
     *  @return List<AttributionTache>
     */
    List<AttributionTache> getAttrTacheLimit(Long idTache, Long idInge, Double nbJour);



    /**
     *  Get the count of joursattribues from an "id" user.
     *
     *
     *  @param idTache the id of the Tache
     *
     *  @return Float
     */
    float getMaxJours(Long idTache);

}



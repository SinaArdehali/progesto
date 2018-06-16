package org.ocdm.service;

import org.ocdm.domain.Projet;
import org.ocdm.domain.Tache;

import java.util.List;

/**
 * Service Interface for managing Projet.
 */
public interface ProjetService {

    /**
     * Save a projet.
     *
     * @param projet the entity to save
     * @return the persisted entity
     */
    Projet save(Projet projet);



//    /**
//     * update the nbJoursVendus of projetMere d'une tache.
//     *
//     * @param tache the entity to save
//     * @return the persisted entity
//     */
//    void update(Tache tache);


    /**
     *  Get all the projets.
     *
     *  @return the list of entities
     */
    List<Projet> findAll();

    /**
     *  Get the "id" projet.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Projet findOne(Long id);

    /**
     *  Delete the "id" projet.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

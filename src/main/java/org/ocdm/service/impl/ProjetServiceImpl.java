package org.ocdm.service.impl;

import org.ocdm.domain.Tache;
import org.ocdm.service.ProjetService;
import org.ocdm.domain.Projet;
import org.ocdm.repository.ProjetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Projet.
 */
@Service
@Transactional
public class ProjetServiceImpl implements ProjetService{

    private final Logger log = LoggerFactory.getLogger(ProjetServiceImpl.class);

    private final ProjetRepository projetRepository;
    public ProjetServiceImpl(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    /**
     * Save a projet.
     *
     * @param projet the entity to save
     * @return the persisted entity
     */
    @Override
    public Projet save(Projet projet) {
        log.debug("Request to save Projet : {}", projet);
        return projetRepository.save(projet);
    }

//    /**
//     * Save a projet.
//     *
//     * @param tache the entity to save
//     * @return the persisted entity
//     */
//    @Override
//    public void update(Tache tache) {
//        log.debug("Request to update Projet a prtir d'un projet: {}", tache);
//        projetRepository.majJoursVendProj(tache);
//    }



    /**
     *  Get all the projets.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Projet> findAll() {
        log.debug("Request to get all Projets");
        return projetRepository.findAllWithEagerRelationships();
    }

    /**
     *  Get one projet by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Projet findOne(Long id) {
        log.debug("Request to get Projet : {}", id);
        return projetRepository.findOneWithEagerRelationships(id);
    }

    /**
     *  Delete the  projet by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Projet : {}", id);
        projetRepository.delete(id);
    }
}

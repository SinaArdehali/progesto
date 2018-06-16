package org.ocdm.service.impl;

import org.ocdm.service.AttributionTacheService;
import org.ocdm.domain.AttributionTache;
import org.ocdm.repository.AttributionTacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ocdm.config.Constants;

/**
 * Service Implementation for managing AttributionTache.
 */
@Service
@Transactional
public class AttributionTacheServiceImpl implements AttributionTacheService{



    private final Logger log = LoggerFactory.getLogger(AttributionTacheServiceImpl.class);

    private final AttributionTacheRepository attributionTacheRepository;

    public AttributionTacheServiceImpl(AttributionTacheRepository attributionTacheRepository) {
        this.attributionTacheRepository = attributionTacheRepository;
    }



    /**
     * Save a attributionTache.
     *
     * @param attributionTache the entity to save
     * @return the persisted entity
     */
    @Override
    public AttributionTache save(AttributionTache attributionTache) {
        log.debug("Request to save AttributionTache : {}", attributionTache);
        return attributionTacheRepository.save(attributionTache);
    }

    /**
     *  Get all the attributionTaches.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AttributionTache> findAll() {
        log.debug("Request to get all AttributionTaches");
        return attributionTacheRepository.findAll();
    }

    /**
     *  Get one attributionTache by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AttributionTache findOne(Long id) {
        log.debug("Request to get AttributionTache : {}", id);
        return attributionTacheRepository.findOne(id);
    }

    /**
     *  Delete the  attributionTache by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttributionTache : {}", id);
        attributionTacheRepository.delete(id);
    }


    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param id the id of the projet to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = true)
    public float getNbJoursAttrib(Long idInge, Long idTache) {
        log.debug("Request to get the sum of joursattribues from an id user. : {}", idInge, idTache);
        return attributionTacheRepository.getNbJoursAttrib(idInge, idTache);
    }


    /**
     * affectationJoursInge a attributionTache.
     *
     * @param idTache the entity to save
     * @return the persisted entity
     */
    @Override
    public int getFirstNRows(Long idTache) {
        log.debug("Request to save  affectationJoursInge AttributionTache : {}", idTache);
//        List<AttributionTache> resultat = attributionTacheRepository.getFirstNRows(idTache);
//        resultat.setMaxResults(2);
        return attributionTacheRepository.getFirstNRows(idTache);
    }



//cree par moiSina
    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param idInge the id of the inge to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = true)
    public float nbJoursDispM(Long idInge) {
        log.debug("Request to get the sum of joursattribues from an id user. : {}", idInge);
        return attributionTacheRepository.nbJoursDispM(idInge);
    }

    //cree par moiSina
    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param idInge the id of the inge to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = true)
    public float nbJoursDispM1(Long idInge) {
        log.debug("Request to get the sum of joursattribues from an id user. : {}", idInge);
        return attributionTacheRepository.nbJoursDispM1(idInge);
    }

    //cree par moiSina
    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param idInge the id of the inge to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = true)
    public float nbJoursDispM2(Long idInge) {
        log.debug("Request to get the sum of joursattribues from an id user. : {}", idInge);
        return attributionTacheRepository.nbJoursDispM2(idInge);
    }

    //cree par moiSina
    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param idInge the id of the inge to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = false)
    public List<AttributionTache> getAttrTacheLimit(Long idTache, Long idInge, Double nbJour) {



        List<AttributionTache> resultSelectLimit;
        int nombreDeLignesAffectees;
        Set<Long> setIdResultSelectLimit = new HashSet<Long>();


        log.debug("Request to get the sum of joursattribues from an id user. : {}", idTache);

//        ici nous multiplions le nbjour par 4 car il s'agit en realite de quart de journee'
        Double nbQuartJourneeFloat = nbJour * Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
        int nbQuartJourneeInt = nbQuartJourneeFloat.intValue();


        resultSelectLimit =  attributionTacheRepository.getAttrTacheLimit(idTache, new PageRequest(0,nbQuartJourneeInt));

System.out.println(resultSelectLimit.toString());

        resultSelectLimit.forEach( item->{
            setIdResultSelectLimit.add(item.getId());
        });
        System.out.println("impression de mes 3 variables : " + "idTache : " + idTache + "idInge : " + idInge + "nbJour : " + nbQuartJourneeInt);
        System.out.println("impression de mon Set setIdResultSelectLimit : |||||||||||||||||||||||" + setIdResultSelectLimit.toString());
//        ici , je dois faire un update sur ma List resultSelectLimit;
        attributionTacheRepository.updateResultSelectLimit(idInge, setIdResultSelectLimit);
        //System.out.println("combien de lignes ont ete affectees |||||||||||||||||||||||" + nombreDeLignesAffectees);

        List<LocalDate> nProchainsJours = new ArrayList<LocalDate>();
        LocalDate today = LocalDate.now();
        for (int i = 0 ; i<nbJour; i++){
            nProchainsJours.add(today.plusDays(i));
        }

        for(LocalDate model : nProchainsJours) {
            System.out.println("impression de mon nProchainsJours.toString() : " + model.toString());
        }

        for (int i = 0; i <nbJour; i++){
            for (int j= 0; j< Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;  j++){
                System.out.println("##" + nProchainsJours.get(i));
                attributionTacheRepository.setDateAttrib(idInge, idTache, nProchainsJours.get(i));
            }
        }




        return resultSelectLimit;

    }


    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param idTache the id of the inge to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = true)
    public float getMaxJours(Long idTache) {
        System.out.println(idTache);

        log.debug("Request to get the sum of joursattribues from an id user. : {}", idTache);
        float nombreMaxJoursEnQuartDeJour =  attributionTacheRepository.getMaxJours(idTache);

        //ci dessous, nous allons convertir ce nombre de quart de jour en journee

        System.out.println("Voici ma constatne a afficher £££" + Constants.CONVERSIONJOURQUARTJOURNEEFLOAT);

        float nombreMaxJoursEnJour = nombreMaxJoursEnQuartDeJour / Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
        return nombreMaxJoursEnJour;
    }
}


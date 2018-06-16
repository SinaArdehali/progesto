package org.ocdm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ocdm.domain.AttributionTache;
import org.ocdm.domain.Projet;
import org.ocdm.domain.Tache;
import org.ocdm.service.ProjetService;
import org.ocdm.service.TacheService;
import org.ocdm.service.dto.EmplMM1M2DTO;
import org.ocdm.service.dto.TacheEtDonneesDTO;
import org.ocdm.service.dto.TacheEtDonneesDTOComplet;
import org.ocdm.service.impl.ProjetServiceImpl;
import org.ocdm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tache.
 */
@RestController
@RequestMapping("/api")
public class TacheResource {

    private final Logger log = LoggerFactory.getLogger(TacheResource.class);

    private static final String ENTITY_NAME = "tache";

    private final TacheService tacheService;
    public TacheResource(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    /**
     * POST  /taches : Create a new tache.
     *
     * @param tache the tache to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tache, or with status 400 (Bad Request) if the tache has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/taches")
    @Timed
    public ResponseEntity<Tache> createTache(@RequestBody Tache tache) throws URISyntaxException {
        log.debug("REST request to save Tache : {}", tache);
        if (tache.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tache cannot already have an ID")).body(null);
        }
        Tache result = tacheService.save(tache);
        return ResponseEntity.created(new URI("/api/taches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taches : Updates an existing tache.
     *
     * @param tache the tache to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tache,
     * or with status 400 (Bad Request) if the tache is not valid,
     * or with status 500 (Internal Server Error) if the tache couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/taches")
    @Timed
    public ResponseEntity<Tache> updateTache(@RequestBody Tache tache) throws URISyntaxException {
        log.debug("REST request to update Tache : {}", tache);
        if (tache.getId() == null) {
            return createTache(tache);
        }
        Tache result = tacheService.save(tache);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tache.getId().toString()))
            .body(result);
    }



    /**
     * GET  /taches : get all the taches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taches in body
     */
    @GetMapping("/taches")
    @Timed
    public List<Tache> getAllTaches() {
        log.debug("REST request to get all Taches");
        return tacheService.findAll();
        }

    /**
     * GET  /taches/:id : get the "id" tache.
     *
     * @param id the id of the tache to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/taches/{id}")
    @Timed
    public ResponseEntity<Tache> getTache(@PathVariable Long id) {
        log.debug("REST request to get Tache : {}", id);
        Tache tache = tacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tache));
    }

    /**
     * GET  /taches/projet/:id : get all tasks related to the "id".
     *
     * @param id the id of the getNbJoursAttribproject to retrieve tasks from.
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/taches/projet/{id}")
    @Timed
    public List<Tache> getTacheByProjetId(@PathVariable Long id) {
        log.debug("REST request to get all Tache related to the project id : {}", id);
        List<Tache> tacheList = tacheService.getAllByProjId(id);
        return tacheList;
    }



    /**
     * GET  /taches/projet/:id : get all tasks related to the "id".
     *
     * @param login the login of the getNbJoursAttribproject to retrieve tasks from.
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/taches/tachesCurrentUser/{login}")
    @Timed
    public List<Tache> getTachesCurrentUser(@PathVariable String login) {
        log.debug("REST request to get all Tache belonged by an user : {}", login);
        List<Tache> tacheCurrentUser = tacheService.getTachesCurrentUser(login);
        return tacheCurrentUser;
    }






    /**
     * DELETE  /taches/:id : delete the "id" tache.
     *
     * @param id the id of the tache to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/taches/{id}")
    @Timed
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        log.debug("REST request to delete Tache : {}", id);
        tacheService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }






    /**
     * GET  /taches/selectPuisRetirerJoursVendus/:nbJour/:idTacheMere : get all users and his available days at M.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/taches/selectPuisRetirerJoursVendus/{nbJour}/{idTacheMere}/")
    @Timed
    public float selectPuisRetirerJoursVendus(@PathVariable  Double nbJour, @PathVariable  Long idTacheMere) throws URISyntaxException {
        System.out.println("-----------------------------------------dans lapi-------------------------------------------- nbJour" + nbJour + " " + idTacheMere);
        log.debug("REST request to get all non affectees users related to the tache : {}", nbJour, idTacheMere);
        float res = tacheService.selectPuisRetirerJoursVendus(nbJour, idTacheMere);
        System.out.println("ce que je recupere dans le retirn . je sui sdans tacheResource au moment du retour " + res);
        return res;
    }



    /**
     * GET  /taches/modifierJoursVendus/:nbJour/:idTacheMere/ : modifier le nombre de jours vendus.
     *
     * @param nbJour the tache to create
     * @param idTacheMere the tacheMere to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tache, or with status 400 (Bad Request) if the tache has already an ID
     *
     */
    @GetMapping("/taches/modifierJoursVendus/{nbJour}/{idTacheMere}")
    @Timed
    public List<AttributionTache> modifierJoursVendus(@PathVariable Long nbJour, @PathVariable Long idTacheMere) throws URISyntaxException {
        List<AttributionTache> attributionTacheResult = new ArrayList<AttributionTache>();
        log.debug("REST request to modify nb jours vendus : {}", nbJour, idTacheMere);
        attributionTacheResult = tacheService.modifierJoursVendus(nbJour, idTacheMere);
        return attributionTacheResult;
    }



    /**
     * GET  /taches/projetBIS/:idProjet : get all tasks related to the "id".
     *
     * @param idProjet the id of the projet to retrieve tasks from.
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/taches/projetBIS/{idProjet}")
    @Timed
    public List<TacheEtDonneesDTOComplet> getTacheByProjetIdBIS(@PathVariable Long idProjet) {
        log.debug("REST request to get all Tache related to the project id : {}", idProjet);
        //Projet tempProjet = projetService.findOne(1001L);
        //log.debug("REST request to get all Tache related to the tempProjet : {}", tempProjet);
        System.out.println(" resultat de mon tacheService.getAllByProjIdBIS(idProjet) nouvelle" + tacheService.getAllByProjIdBIS(idProjet));
        List<TacheEtDonneesDTOComplet> tacheList = tacheService.getAllByProjIdBIS(idProjet);
        System.out.println(" quel est le contenu de ma sortie de tacheService @@@@@@@@@@@ : " + tacheList.size());
        return tacheList;
    }




}

//COMMENTAIRE A SUPPRIMER

//private final ProjetService projetService;

//public TacheResource(TacheService tacheService, ProjetService projetService) {




//        //    cree par moiSina
//    //    l'api ci dessous sert a mettre a jour le champ projet.joursVendusProjet du projet en fonction de la somme
//    //    des jours vendus de toutes les taches ayant pour projetmere ce projet
//        /**
//         * PUT  /projets/tache : Updates the field joursVendusProjet from the projetMere of an existing tache.
//         *
//         * @param tache the tache to retrieve projetMere from.
//         * @return the ResponseEntity with status 200 (OK) and with body the updated projet,
//         * or with status 400 (Bad Request) if the projet is not valid,
//         * or with status 500 (Internal Server Error) if the projet couldn't be updated
//         * @throws URISyntaxException if the Location URI syntax is incorrect
//         */
//        @PutMapping("/projets/tache")
//        @Timed
//        public ResponseEntity<Projet> updateProjet(@RequestBody Tache tache) throws URISyntaxException {
//            log.debug("REST request to update Projet : {}", tache);
//    //        if (projet.getId() == null) {
//    //            return createProjet(projet);
//    //        }
//            Projet result = projetService.update(tache);
//    //        mettre a jour la ligne ci dessous
//    //        attentuion, ci dessous, j'ai change le projet.getId() et j'ai mis tache.getId()
//            return ResponseEntity.ok()
//                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tache.getId().toString()))
//                .body(result);
//        }
//    //fin cree par moiSina




//    /**
//     * POST  /taches/projet/:projet : get all tasks related to the "projet".
//     *
//     * @param projet the project to retrieve tasks from
//     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
//     */
//    @PostMapping("/taches/projet/")
//    @Timed
//    public List<Tache> getTacheByProjet(@RequestBody Projet projet) {
//        log.debug("REST request to get all Tache related to the project : {}", projet);
//        //Projet tempProjet = projetService.findOne(1001L);
//        //log.debug("REST request to get all Tache related to the tempProjet : {}", tempProjet);
//
//
//        List<Tache> tacheList = tacheService.findAllByProjet(projet);
//
//
//        //List<Tache> tacheList = new ArrayList<>();
//        return tacheList;
//        //return tacheList;
//    }




// JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR A PRESENT LA MAJ DU PROJET EN MEME TEMPS QUE LA MAJ DE LA TACHE'
////cree par moiSina
//    /**
//     * PUT  /taches/projet : Updates the field joursVendusProjet from the projetMere of an existing tache.
//     *
//     * @param tache the tache to retrieve projetMere from.
//     * @return the ResponseEntity with status 200 (OK) and with body the updated projet,
//     * or with status 400 (Bad Request) if the projet is not valid,
//     * or with status 500 (Internal Server Error) if the projet couldn't be updated
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PutMapping("/taches/projet")
//    @Timed
//    public ResponseEntity<Projet> majJoursVendProj(@RequestBody Tache tache) throws URISyntaxException {
////    System.out.println("-----------------------------------------dans lapi--------------------------------------------");
////        System.out.println(tache);
//        log.debug("REST request to update Projet : {}", tache);
////        if (tache.getId() == null) {
////            return createTache(tache);
////        }
//        int result = tacheService.majJoursVendProj(tache);
//        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tache.getId().toString())).build();
//    }
// //   dans cette methode, j'heiste sur le fait de devoir retourner un void ou un projet. a la ligne 227, jhesite aussi sur le toDtring et le body(result)
////    fin cree par moiSina



//    // JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR LE BLOC CI DESSOUS CORRESPOND A LA LIGNE TOTAL QUE J'AI INSERE ET QUI SE DEPLOYAIT QUAND ON CLIQUAIT SUR "VOIR LES TACHES DU PROJET"
//    /**
//     * GET  /taches/sumjoursvendus/:id : get the sum from an "id" project.
//     *
//     * @param id the id of the project to retrieve sum of all the taches
//     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
//     */
//    @GetMapping("/taches/sumjoursvendus/{id}")
//    @Timed
//    public ResponseEntity<Float> getSumJoursVendus(@PathVariable Long id) {
//        log.debug("REST request to get sum of all the taches : {}", id);
//        float res = tacheService.trouverSumJoursVendus(id);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
//    }







//    /**
//     * GET  /taches/recupererTousTacheAvecDonnees/:idProjet : get all users and his available days at M.
//     *
//     * @return the ResponseEntity with status 200 (OK) and with body all users
//     */
//    @GetMapping("/taches/recupererTousTacheAvecDonnees/{idProjet}")
//    @Timed
//    public List<TacheEtDonneesDTOComplet> getJoursAttrByTache(Long idProjet) {
//        System.out.println();
//        log.debug("REST request to get all non affectees users related to the tache : {}");
//        List<TacheEtDonneesDTOComplet> res = tacheService.getJoursAttrByTache(idProjet);
//        return res;
//    }





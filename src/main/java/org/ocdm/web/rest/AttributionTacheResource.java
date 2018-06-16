package org.ocdm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ocdm.domain.AttributionTache;
import org.ocdm.service.AttributionTacheService;
import org.ocdm.service.dto.EmplMM1M2DTO;
import org.ocdm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AttributionTache.
 */
@RestController
@RequestMapping("/api")
public class AttributionTacheResource {

    private final Logger log = LoggerFactory.getLogger(AttributionTacheResource.class);

    private static final String ENTITY_NAME = "attributionTache";

    private final AttributionTacheService attributionTacheService;

    public AttributionTacheResource(AttributionTacheService attributionTacheService) {
        this.attributionTacheService = attributionTacheService;
    }

    /**
     * POST  /attribution-taches : Create a new attributionTache.
     *
     * @param attributionTache the attributionTache to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attributionTache, or with status 400 (Bad Request) if the attributionTache has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attribution-taches")
    @Timed
    public ResponseEntity<AttributionTache> createAttributionTache(@RequestBody AttributionTache attributionTache) throws URISyntaxException {
        log.debug("REST request to save AttributionTache : {}", attributionTache);
        if (attributionTache.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attributionTache cannot already have an ID")).body(null);
        }
        AttributionTache result = attributionTacheService.save(attributionTache);
        return ResponseEntity.created(new URI("/api/attribution-taches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attribution-taches : Updates an existing attributionTache.
     *
     * @param attributionTache the attributionTache to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attributionTache,
     * or with status 400 (Bad Request) if the attributionTache is not valid,
     * or with status 500 (Internal Server Error) if the attributionTache couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attribution-taches")
    @Timed
    public ResponseEntity<AttributionTache> updateAttributionTache(@RequestBody AttributionTache attributionTache) throws URISyntaxException {
        log.debug("REST request to update AttributionTache : {}", attributionTache);
        if (attributionTache.getId() == null) {
            return createAttributionTache(attributionTache);
        }
        AttributionTache result = attributionTacheService.save(attributionTache);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attributionTache.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attribution-taches : get all the attributionTaches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributionTaches in body
     */
    @GetMapping("/attribution-taches")
    @Timed
    public List<AttributionTache> getAllAttributionTaches() {
        log.debug("REST request to get all AttributionTaches");
        return attributionTacheService.findAll();
    }

    /**
     * GET  /attribution-taches/:id : get the "id" attributionTache.
     *
     * @param id the id of the attributionTache to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attributionTache, or with status 404 (Not Found)
     */
    @GetMapping("/attribution-taches/{id}")
    @Timed
    public ResponseEntity<AttributionTache> getAttributionTache(@PathVariable Long id) {
        log.debug("REST request to get AttributionTache : {}", id);
        AttributionTache attributionTache = attributionTacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attributionTache));
    }

    /**
     * DELETE  /attribution-taches/:id : delete the "id" attributionTache.
     *
     * @param id the id of the attributionTache to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attribution-taches/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttributionTache(@PathVariable Long id) {
        log.debug("REST request to delete AttributionTache : {}", id);
        attributionTacheService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }



//rajoute par moiSina'
    /**
     //     * GET  /attribution-taches/nbjoursattribues/:id : get the sum of joursattribues from an "id" user.
     //     *
     //     * @param id the id of the user to retrieve count of all the jours attribues
     //     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     //     */
    @GetMapping("/attribution-taches/nbjoursattribues/{idInge}/{idTache}")
    @Timed
    public ResponseEntity<Float> getNbJoursAttrib(@PathVariable Long idInge,@PathVariable  Long idTache) {
        log.debug("REST request to get the sum of joursattribues from an id user : {}", idInge, idTache);
        float res = attributionTacheService.getNbJoursAttrib(idInge, idTache);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }


//rajoute par moiSina'
    /**
     //     * PUT  /attribution-taches/affectationJoursInge/:id : get the sum of joursattribues from an "id" user.
     //     *
     //     * @param id the id of the user to retrieve count of all the jours attribues
     //     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     //     */
    @PutMapping("/attribution-taches/affectationJoursInge/{idTache}")
    @Timed
    public int affectationJoursInge(@RequestBody  Long idTache) throws URISyntaxException {
        log.debug("REST request to update AttributionTache : {}", idTache);

        return attributionTacheService.getFirstNRows(idTache);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, idTache.toString()))
//            .body(result);
    }



    //rajoute par moiSina'
    /**
     //     * GET  /attribution-taches/nbJoursDispM/:idInge : get the sum of joursattribues from an "id" user.
     //     *
     //     * @param id the id of the user to retrieve count of all the jours attribues
     //     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     //     */
    @GetMapping("/attribution-taches/nbJoursDispM/{idInge}")
    @Timed
    public ResponseEntity<Float> nbJoursDispM(@PathVariable  Long idInge) {
        log.debug("REST request to get the sum of joursattribues from an id user : {}", idInge);
        float res = attributionTacheService.nbJoursDispM(idInge);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    //rajoute par moiSina'
    /**
     //     * GET  /attribution-taches/nbJoursDispM/:idInge : get the sum of joursattribues from an "id" user.
     //     *
     //     * @param id the id of the user to retrieve count of all the jours attribues
     //     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     //     */
    @GetMapping("/attribution-taches/nbJoursDispM1/{idInge}")
    @Timed
    public ResponseEntity<Float> nbJoursDispM1(@PathVariable  Long idInge) {
        log.debug("REST request to get the sum of joursattribues from an id user : {}", idInge);
        float res = attributionTacheService.nbJoursDispM(idInge);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    //rajoute par moiSina'
    /**
     //     * GET  /attribution-taches/nbJoursDispM/:idInge : get the sum of joursattribues from an "id" user.
     //     *
     //     * @param id the id of the user to retrieve count of all the jours attribues
     //     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     //     */
    @GetMapping("/attribution-taches/nbJoursDispM2/{idInge}")
    @Timed
    public ResponseEntity<Float> nbJoursDispM2(@PathVariable  Long idInge) {
        log.debug("REST request to get the sum of joursattribues from an id user : {}", idInge);
        float res = attributionTacheService.nbJoursDispM(idInge);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }


    /**
     * GET  /attribution-taches/getAttrTacheLimit/{idTache}/{idInge}/{nbJour} : get all users and his available days at M.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/attribution-taches/getAttrTacheLimit/{idTache}/{idInge}/{nbJour}/")
    @Timed
    public List<AttributionTache> getAttrTacheLimit(@PathVariable  Long idTache, @PathVariable  Long idInge, @PathVariable  Double nbJour) {
        System.out.println("impression de ma variale nbJour//////////////////////////" + nbJour);
        log.debug("REST request to get all non affectees users related to the tache : {}", idTache, idInge, nbJour);
        List<AttributionTache> res = attributionTacheService.getAttrTacheLimit(idTache, idInge, nbJour);
        System.out.println("impression de ma liste res }}}}}}}}}}}}}}}:");
        res.forEach(System.out::println);
        return res;
    }

    /**
     * GET  /attribution-taches/getMaxJours/{idTache} : get all users and his available days at M.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/attribution-taches/getMaxJours/{idTache}")
    @Timed
    public ResponseEntity<Float> getMaxJours(@PathVariable Long idTache) {
        System.out.println();
        log.debug("REST request to get all non affectees users related to the tache : {}", idTache);
        float res = attributionTacheService.getMaxJours(idTache);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }


}

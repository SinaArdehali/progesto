package org.ocdm.web.rest;

import org.ocdm.ProgestoApp;

import org.ocdm.domain.Tache;
import org.ocdm.repository.TacheRepository;
import org.ocdm.service.ProjetService;
import org.ocdm.service.TacheService;
import org.ocdm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TacheResource REST controller.
 *
 * @see TacheResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProgestoApp.class)
public class TacheResourceIntTest {

    private static final String DEFAULT_NOM_TACHE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TACHE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_TACHE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_TACHE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEBUT_TACHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEBUT_TACHE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FIN_TACHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIN_TACHE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NB_QUART_JOUR_ATTRIBUER = 1;
    private static final Integer UPDATED_NB_QUART_JOUR_ATTRIBUER = 2;

    private static final Float DEFAULT_JOURS_VENDUS_TACHE = 1F;
    private static final Float UPDATED_JOURS_VENDUS_TACHE = 2F;

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private TacheService tacheService;

    /*@Autowired
    private ProjetService projetService;*/

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTacheMockMvc;

    private Tache tache;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //final TacheResource tacheResource = new TacheResource(tacheService, projetService);
        final TacheResource tacheResource = new TacheResource(tacheService);
        this.restTacheMockMvc = MockMvcBuilders.standaloneSetup(tacheResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tache createEntity(EntityManager em) {
        Tache tache = new Tache()
            .nomTache(DEFAULT_NOM_TACHE)
            .descriptionTache(DEFAULT_DESCRIPTION_TACHE)
            .debutTache(DEFAULT_DEBUT_TACHE)
            .finTache(DEFAULT_FIN_TACHE)
            .nbQuartJourAttribuer(DEFAULT_NB_QUART_JOUR_ATTRIBUER)
            .joursVendusTache(DEFAULT_JOURS_VENDUS_TACHE);
        return tache;
    }

    @Before
    public void initTest() {
        tache = createEntity(em);
    }

    @Test
    @Transactional
    public void createTache() throws Exception {
        int databaseSizeBeforeCreate = tacheRepository.findAll().size();

        // Create the Tache
        restTacheMockMvc.perform(post("/api/taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tache)))
            .andExpect(status().isCreated());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeCreate + 1);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getNomTache()).isEqualTo(DEFAULT_NOM_TACHE);
        assertThat(testTache.getDescriptionTache()).isEqualTo(DEFAULT_DESCRIPTION_TACHE);
        assertThat(testTache.getDebutTache()).isEqualTo(DEFAULT_DEBUT_TACHE);
        assertThat(testTache.getFinTache()).isEqualTo(DEFAULT_FIN_TACHE);
        assertThat(testTache.getNbQuartJourAttribuer()).isEqualTo(DEFAULT_NB_QUART_JOUR_ATTRIBUER);
        assertThat(testTache.getJoursVendusTache()).isEqualTo(DEFAULT_JOURS_VENDUS_TACHE);
    }

    @Test
    @Transactional
    public void createTacheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tacheRepository.findAll().size();

        // Create the Tache with an existing ID
        tache.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTacheMockMvc.perform(post("/api/taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tache)))
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTaches() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get all the tacheList
        restTacheMockMvc.perform(get("/api/taches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTache").value(hasItem(DEFAULT_NOM_TACHE.toString())))
            .andExpect(jsonPath("$.[*].descriptionTache").value(hasItem(DEFAULT_DESCRIPTION_TACHE.toString())))
            .andExpect(jsonPath("$.[*].debutTache").value(hasItem(DEFAULT_DEBUT_TACHE.toString())))
            .andExpect(jsonPath("$.[*].finTache").value(hasItem(DEFAULT_FIN_TACHE.toString())))
            .andExpect(jsonPath("$.[*].nbQuartJourAttribuer").value(hasItem(DEFAULT_NB_QUART_JOUR_ATTRIBUER)))
            .andExpect(jsonPath("$.[*].joursVendusTache").value(hasItem(DEFAULT_JOURS_VENDUS_TACHE.doubleValue())));
    }

    @Test
    @Transactional
    public void getTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get the tache
        restTacheMockMvc.perform(get("/api/taches/{id}", tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tache.getId().intValue()))
            .andExpect(jsonPath("$.nomTache").value(DEFAULT_NOM_TACHE.toString()))
            .andExpect(jsonPath("$.descriptionTache").value(DEFAULT_DESCRIPTION_TACHE.toString()))
            .andExpect(jsonPath("$.debutTache").value(DEFAULT_DEBUT_TACHE.toString()))
            .andExpect(jsonPath("$.finTache").value(DEFAULT_FIN_TACHE.toString()))
            .andExpect(jsonPath("$.nbQuartJourAttribuer").value(DEFAULT_NB_QUART_JOUR_ATTRIBUER))
            .andExpect(jsonPath("$.joursVendusTache").value(DEFAULT_JOURS_VENDUS_TACHE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTache() throws Exception {
        // Get the tache
        restTacheMockMvc.perform(get("/api/taches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTache() throws Exception {
        // Initialize the database
        tacheService.save(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache
        Tache updatedTache = tacheRepository.findOne(tache.getId());
        updatedTache
            .nomTache(UPDATED_NOM_TACHE)
            .descriptionTache(UPDATED_DESCRIPTION_TACHE)
            .debutTache(UPDATED_DEBUT_TACHE)
            .finTache(UPDATED_FIN_TACHE)
            .nbQuartJourAttribuer(UPDATED_NB_QUART_JOUR_ATTRIBUER)
            .joursVendusTache(UPDATED_JOURS_VENDUS_TACHE);

        restTacheMockMvc.perform(put("/api/taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTache)))
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getNomTache()).isEqualTo(UPDATED_NOM_TACHE);
        assertThat(testTache.getDescriptionTache()).isEqualTo(UPDATED_DESCRIPTION_TACHE);
        assertThat(testTache.getDebutTache()).isEqualTo(UPDATED_DEBUT_TACHE);
        assertThat(testTache.getFinTache()).isEqualTo(UPDATED_FIN_TACHE);
        assertThat(testTache.getNbQuartJourAttribuer()).isEqualTo(UPDATED_NB_QUART_JOUR_ATTRIBUER);
        assertThat(testTache.getJoursVendusTache()).isEqualTo(UPDATED_JOURS_VENDUS_TACHE);
    }

    @Test
    @Transactional
    public void updateNonExistingTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Create the Tache

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTacheMockMvc.perform(put("/api/taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tache)))
            .andExpect(status().isCreated());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTache() throws Exception {
        // Initialize the database
        tacheService.save(tache);

        int databaseSizeBeforeDelete = tacheRepository.findAll().size();

        // Get the tache
        restTacheMockMvc.perform(delete("/api/taches/{id}", tache.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tache.class);
        Tache tache1 = new Tache();
        tache1.setId(1L);
        Tache tache2 = new Tache();
        tache2.setId(tache1.getId());
        assertThat(tache1).isEqualTo(tache2);
        tache2.setId(2L);
        assertThat(tache1).isNotEqualTo(tache2);
        tache1.setId(null);
        assertThat(tache1).isNotEqualTo(tache2);
    }
}

package org.ocdm.web.rest;

import org.ocdm.ProgestoApp;

import org.ocdm.domain.Projet;
import org.ocdm.repository.ProjetRepository;
import org.ocdm.service.ProjetService;
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
 * Test class for the ProjetResource REST controller.
 *
 * @see ProjetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProgestoApp.class)
public class ProjetResourceIntTest {

    private static final String DEFAULT_NOM_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROJET = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_PROJET = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEBUT_PROJET = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEBUT_PROJET = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FIN_PROJET = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIN_PROJET = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_JOURS_VENDUS_PROJET = 1F;
    private static final Float UPDATED_JOURS_VENDUS_PROJET = 2F;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private ProjetService projetService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjetMockMvc;

    private Projet projet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjetResource projetResource = new ProjetResource(projetService, projetRepository);
        this.restProjetMockMvc = MockMvcBuilders.standaloneSetup(projetResource)
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
    public static Projet createEntity(EntityManager em) {
        Projet projet = new Projet()
            .nomProjet(DEFAULT_NOM_PROJET)
            .descriptionProjet(DEFAULT_DESCRIPTION_PROJET)
            .debutProjet(DEFAULT_DEBUT_PROJET)
            .finProjet(DEFAULT_FIN_PROJET)
            .joursVendusProjet(DEFAULT_JOURS_VENDUS_PROJET);
        return projet;
    }

    @Before
    public void initTest() {
        projet = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjet() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // Create the Projet
        restProjetMockMvc.perform(post("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate + 1);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjet.getDescriptionProjet()).isEqualTo(DEFAULT_DESCRIPTION_PROJET);
        assertThat(testProjet.getDebutProjet()).isEqualTo(DEFAULT_DEBUT_PROJET);
        assertThat(testProjet.getFinProjet()).isEqualTo(DEFAULT_FIN_PROJET);
        assertThat(testProjet.getJoursVendusProjet()).isEqualTo(DEFAULT_JOURS_VENDUS_PROJET);
    }

    @Test
    @Transactional
    public void createProjetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // Create the Projet with an existing ID
        projet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetMockMvc.perform(post("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjets() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList
        restProjetMockMvc.perform(get("/api/projets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProjet").value(hasItem(DEFAULT_NOM_PROJET.toString())))
            .andExpect(jsonPath("$.[*].descriptionProjet").value(hasItem(DEFAULT_DESCRIPTION_PROJET.toString())))
            .andExpect(jsonPath("$.[*].debutProjet").value(hasItem(DEFAULT_DEBUT_PROJET.toString())))
            .andExpect(jsonPath("$.[*].finProjet").value(hasItem(DEFAULT_FIN_PROJET.toString())))
            .andExpect(jsonPath("$.[*].joursVendusProjet").value(hasItem(DEFAULT_JOURS_VENDUS_PROJET.doubleValue())));
    }

    @Test
    @Transactional
    public void getProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projet.getId().intValue()))
            .andExpect(jsonPath("$.nomProjet").value(DEFAULT_NOM_PROJET.toString()))
            .andExpect(jsonPath("$.descriptionProjet").value(DEFAULT_DESCRIPTION_PROJET.toString()))
            .andExpect(jsonPath("$.debutProjet").value(DEFAULT_DEBUT_PROJET.toString()))
            .andExpect(jsonPath("$.finProjet").value(DEFAULT_FIN_PROJET.toString()))
            .andExpect(jsonPath("$.joursVendusProjet").value(DEFAULT_JOURS_VENDUS_PROJET.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjet() throws Exception {
        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjet() throws Exception {
        // Initialize the database
        projetService.save(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet
        Projet updatedProjet = projetRepository.findOne(projet.getId());
        updatedProjet
            .nomProjet(UPDATED_NOM_PROJET)
            .descriptionProjet(UPDATED_DESCRIPTION_PROJET)
            .debutProjet(UPDATED_DEBUT_PROJET)
            .finProjet(UPDATED_FIN_PROJET)
            .joursVendusProjet(UPDATED_JOURS_VENDUS_PROJET);

        restProjetMockMvc.perform(put("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjet)))
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjet.getDescriptionProjet()).isEqualTo(UPDATED_DESCRIPTION_PROJET);
        assertThat(testProjet.getDebutProjet()).isEqualTo(UPDATED_DEBUT_PROJET);
        assertThat(testProjet.getFinProjet()).isEqualTo(UPDATED_FIN_PROJET);
        assertThat(testProjet.getJoursVendusProjet()).isEqualTo(UPDATED_JOURS_VENDUS_PROJET);
    }

    @Test
    @Transactional
    public void updateNonExistingProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Create the Projet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjetMockMvc.perform(put("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjet() throws Exception {
        // Initialize the database
        projetService.save(projet);

        int databaseSizeBeforeDelete = projetRepository.findAll().size();

        // Get the projet
        restProjetMockMvc.perform(delete("/api/projets/{id}", projet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projet.class);
        Projet projet1 = new Projet();
        projet1.setId(1L);
        Projet projet2 = new Projet();
        projet2.setId(projet1.getId());
        assertThat(projet1).isEqualTo(projet2);
        projet2.setId(2L);
        assertThat(projet1).isNotEqualTo(projet2);
        projet1.setId(null);
        assertThat(projet1).isNotEqualTo(projet2);
    }
}

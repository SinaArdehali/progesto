package org.ocdm.web.rest;

import org.ocdm.ProgestoApp;

import org.ocdm.domain.AttributionTache;
import org.ocdm.repository.AttributionTacheRepository;
import org.ocdm.service.AttributionTacheService;
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
 * Test class for the AttributionTacheResource REST controller.
 *
 * @see AttributionTacheResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProgestoApp.class)
public class AttributionTacheResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_QUART_JOURNEE = 1;
    private static final Integer UPDATED_QUART_JOURNEE = 2;

    @Autowired
    private AttributionTacheRepository attributionTacheRepository;

    @Autowired
    private AttributionTacheService attributionTacheService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttributionTacheMockMvc;

    private AttributionTache attributionTache;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributionTacheResource attributionTacheResource = new AttributionTacheResource(attributionTacheService);
        this.restAttributionTacheMockMvc = MockMvcBuilders.standaloneSetup(attributionTacheResource)
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
    public static AttributionTache createEntity(EntityManager em) {
        AttributionTache attributionTache = new AttributionTache()
            .date(DEFAULT_DATE)
            .quartJournee(DEFAULT_QUART_JOURNEE);
        return attributionTache;
    }

    @Before
    public void initTest() {
        attributionTache = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttributionTache() throws Exception {
        int databaseSizeBeforeCreate = attributionTacheRepository.findAll().size();

        // Create the AttributionTache
        restAttributionTacheMockMvc.perform(post("/api/attribution-taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributionTache)))
            .andExpect(status().isCreated());

        // Validate the AttributionTache in the database
        List<AttributionTache> attributionTacheList = attributionTacheRepository.findAll();
        assertThat(attributionTacheList).hasSize(databaseSizeBeforeCreate + 1);
        AttributionTache testAttributionTache = attributionTacheList.get(attributionTacheList.size() - 1);
        assertThat(testAttributionTache.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAttributionTache.getQuartJournee()).isEqualTo(DEFAULT_QUART_JOURNEE);
    }

    @Test
    @Transactional
    public void createAttributionTacheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributionTacheRepository.findAll().size();

        // Create the AttributionTache with an existing ID
        attributionTache.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributionTacheMockMvc.perform(post("/api/attribution-taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributionTache)))
            .andExpect(status().isBadRequest());

        // Validate the AttributionTache in the database
        List<AttributionTache> attributionTacheList = attributionTacheRepository.findAll();
        assertThat(attributionTacheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAttributionTaches() throws Exception {
        // Initialize the database
        attributionTacheRepository.saveAndFlush(attributionTache);

        // Get all the attributionTacheList
        restAttributionTacheMockMvc.perform(get("/api/attribution-taches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributionTache.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].quartJournee").value(hasItem(DEFAULT_QUART_JOURNEE)));
    }

    @Test
    @Transactional
    public void getAttributionTache() throws Exception {
        // Initialize the database
        attributionTacheRepository.saveAndFlush(attributionTache);

        // Get the attributionTache
        restAttributionTacheMockMvc.perform(get("/api/attribution-taches/{id}", attributionTache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attributionTache.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.quartJournee").value(DEFAULT_QUART_JOURNEE));
    }

    @Test
    @Transactional
    public void getNonExistingAttributionTache() throws Exception {
        // Get the attributionTache
        restAttributionTacheMockMvc.perform(get("/api/attribution-taches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttributionTache() throws Exception {
        // Initialize the database
        attributionTacheService.save(attributionTache);

        int databaseSizeBeforeUpdate = attributionTacheRepository.findAll().size();

        // Update the attributionTache
        AttributionTache updatedAttributionTache = attributionTacheRepository.findOne(attributionTache.getId());
        updatedAttributionTache
            .date(UPDATED_DATE)
            .quartJournee(UPDATED_QUART_JOURNEE);

        restAttributionTacheMockMvc.perform(put("/api/attribution-taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttributionTache)))
            .andExpect(status().isOk());

        // Validate the AttributionTache in the database
        List<AttributionTache> attributionTacheList = attributionTacheRepository.findAll();
        assertThat(attributionTacheList).hasSize(databaseSizeBeforeUpdate);
        AttributionTache testAttributionTache = attributionTacheList.get(attributionTacheList.size() - 1);
        assertThat(testAttributionTache.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAttributionTache.getQuartJournee()).isEqualTo(UPDATED_QUART_JOURNEE);
    }

    @Test
    @Transactional
    public void updateNonExistingAttributionTache() throws Exception {
        int databaseSizeBeforeUpdate = attributionTacheRepository.findAll().size();

        // Create the AttributionTache

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttributionTacheMockMvc.perform(put("/api/attribution-taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributionTache)))
            .andExpect(status().isCreated());

        // Validate the AttributionTache in the database
        List<AttributionTache> attributionTacheList = attributionTacheRepository.findAll();
        assertThat(attributionTacheList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttributionTache() throws Exception {
        // Initialize the database
        attributionTacheService.save(attributionTache);

        int databaseSizeBeforeDelete = attributionTacheRepository.findAll().size();

        // Get the attributionTache
        restAttributionTacheMockMvc.perform(delete("/api/attribution-taches/{id}", attributionTache.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttributionTache> attributionTacheList = attributionTacheRepository.findAll();
        assertThat(attributionTacheList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributionTache.class);
        AttributionTache attributionTache1 = new AttributionTache();
        attributionTache1.setId(1L);
        AttributionTache attributionTache2 = new AttributionTache();
        attributionTache2.setId(attributionTache1.getId());
        assertThat(attributionTache1).isEqualTo(attributionTache2);
        attributionTache2.setId(2L);
        assertThat(attributionTache1).isNotEqualTo(attributionTache2);
        attributionTache1.setId(null);
        assertThat(attributionTache1).isNotEqualTo(attributionTache2);
    }
}

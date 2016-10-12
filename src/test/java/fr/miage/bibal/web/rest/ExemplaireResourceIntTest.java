package fr.miage.bibal.web.rest;

import fr.miage.bibal.BibalApp;

import fr.miage.bibal.domain.Exemplaire;
import fr.miage.bibal.repository.ExemplaireRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExemplaireResource REST controller.
 *
 * @see ExemplaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BibalApp.class)
public class ExemplaireResourceIntTest {

    private static final String DEFAULT_ETAT = "AAAAA";
    private static final String UPDATED_ETAT = "BBBBB";

    @Inject
    private ExemplaireRepository exemplaireRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExemplaireMockMvc;

    private Exemplaire exemplaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExemplaireResource exemplaireResource = new ExemplaireResource();
        ReflectionTestUtils.setField(exemplaireResource, "exemplaireRepository", exemplaireRepository);
        this.restExemplaireMockMvc = MockMvcBuilders.standaloneSetup(exemplaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exemplaire createEntity(EntityManager em) {
        Exemplaire exemplaire = new Exemplaire()
                .etat(DEFAULT_ETAT);
        return exemplaire;
    }

    @Before
    public void initTest() {
        exemplaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createExemplaire() throws Exception {
        int databaseSizeBeforeCreate = exemplaireRepository.findAll().size();

        // Create the Exemplaire

        restExemplaireMockMvc.perform(post("/api/exemplaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exemplaire)))
                .andExpect(status().isCreated());

        // Validate the Exemplaire in the database
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        assertThat(exemplaires).hasSize(databaseSizeBeforeCreate + 1);
        Exemplaire testExemplaire = exemplaires.get(exemplaires.size() - 1);
        assertThat(testExemplaire.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    public void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = exemplaireRepository.findAll().size();
        // set the field null
        exemplaire.setEtat(null);

        // Create the Exemplaire, which fails.

        restExemplaireMockMvc.perform(post("/api/exemplaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exemplaire)))
                .andExpect(status().isBadRequest());

        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        assertThat(exemplaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExemplaires() throws Exception {
        // Initialize the database
        exemplaireRepository.saveAndFlush(exemplaire);

        // Get all the exemplaires
        restExemplaireMockMvc.perform(get("/api/exemplaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(exemplaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void getExemplaire() throws Exception {
        // Initialize the database
        exemplaireRepository.saveAndFlush(exemplaire);

        // Get the exemplaire
        restExemplaireMockMvc.perform(get("/api/exemplaires/{id}", exemplaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exemplaire.getId().intValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExemplaire() throws Exception {
        // Get the exemplaire
        restExemplaireMockMvc.perform(get("/api/exemplaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExemplaire() throws Exception {
        // Initialize the database
        exemplaireRepository.saveAndFlush(exemplaire);
        int databaseSizeBeforeUpdate = exemplaireRepository.findAll().size();

        // Update the exemplaire
        Exemplaire updatedExemplaire = exemplaireRepository.findOne(exemplaire.getId());
        updatedExemplaire
                .etat(UPDATED_ETAT);

        restExemplaireMockMvc.perform(put("/api/exemplaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedExemplaire)))
                .andExpect(status().isOk());

        // Validate the Exemplaire in the database
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        assertThat(exemplaires).hasSize(databaseSizeBeforeUpdate);
        Exemplaire testExemplaire = exemplaires.get(exemplaires.size() - 1);
        assertThat(testExemplaire.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void deleteExemplaire() throws Exception {
        // Initialize the database
        exemplaireRepository.saveAndFlush(exemplaire);
        int databaseSizeBeforeDelete = exemplaireRepository.findAll().size();

        // Get the exemplaire
        restExemplaireMockMvc.perform(delete("/api/exemplaires/{id}", exemplaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        assertThat(exemplaires).hasSize(databaseSizeBeforeDelete - 1);
    }
}

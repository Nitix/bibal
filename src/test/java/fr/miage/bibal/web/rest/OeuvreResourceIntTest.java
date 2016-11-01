package fr.miage.bibal.web.rest;

import fr.miage.bibal.BibalApp;

import fr.miage.bibal.domain.Oeuvre;
import fr.miage.bibal.domain.Auteur;
import fr.miage.bibal.repository.OeuvreRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OeuvreResource REST controller.
 *
 * @see OeuvreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BibalApp.class)
public class OeuvreResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAA";
    private static final String UPDATED_TITRE = "BBBBB";

    private static final String DEFAULT_EDITEUR = "AAAAA";
    private static final String UPDATED_EDITEUR = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_DATA_PUBLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PUBLICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EDITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EDITION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final LocalDate DEFAULT_PARUTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PARUTION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_PERIODICITE = 1;
    private static final Integer UPDATED_PERIODICITE = 2;

    private static final Boolean DEFAULT_EST_LIVRE = false;
    private static final Boolean UPDATED_EST_LIVRE = true;

    @Inject
    private OeuvreRepository oeuvreRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOeuvreMockMvc;

    private Oeuvre oeuvre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OeuvreResource oeuvreResource = new OeuvreResource();
        ReflectionTestUtils.setField(oeuvreResource, "oeuvreRepository", oeuvreRepository);
        this.restOeuvreMockMvc = MockMvcBuilders.standaloneSetup(oeuvreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oeuvre createEntity(EntityManager em) {
        Oeuvre oeuvre = new Oeuvre()
                .titre(DEFAULT_TITRE)
                .editeur(DEFAULT_EDITEUR)
                .description(DEFAULT_DESCRIPTION)
                .dataPublication(DEFAULT_DATA_PUBLICATION)
                .dateEdition(DEFAULT_DATE_EDITION)
                .numero(DEFAULT_NUMERO)
                .parution(DEFAULT_PARUTION)
                .periodicite(DEFAULT_PERIODICITE)
                .estLivre(DEFAULT_EST_LIVRE);
        // Add required entity
        Auteur auteur = AuteurResourceIntTest.createEntity(em);
        em.persist(auteur);
        em.flush();
        oeuvre.setAuteur(auteur);
        return oeuvre;
    }

    @Before
    public void initTest() {
        oeuvre = createEntity(em);
    }

    @Test
    @Transactional
    public void createOeuvre() throws Exception {
        int databaseSizeBeforeCreate = oeuvreRepository.findAll().size();

        // Create the Oeuvre

        restOeuvreMockMvc.perform(post("/api/oeuvres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(oeuvre)))
                .andExpect(status().isCreated());

        // Validate the Oeuvre in the database
        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeCreate + 1);
        Oeuvre testOeuvre = oeuvres.get(oeuvres.size() - 1);
        assertThat(testOeuvre.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testOeuvre.getEditeur()).isEqualTo(DEFAULT_EDITEUR);
        assertThat(testOeuvre.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOeuvre.getDataPublication()).isEqualTo(DEFAULT_DATA_PUBLICATION);
        assertThat(testOeuvre.getDateEdition()).isEqualTo(DEFAULT_DATE_EDITION);
        assertThat(testOeuvre.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testOeuvre.getParution()).isEqualTo(DEFAULT_PARUTION);
        assertThat(testOeuvre.getPeriodicite()).isEqualTo(DEFAULT_PERIODICITE);
        assertThat(testOeuvre.isEstLivre()).isEqualTo(DEFAULT_EST_LIVRE);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = oeuvreRepository.findAll().size();
        // set the field null
        oeuvre.setTitre(null);

        // Create the Oeuvre, which fails.

        restOeuvreMockMvc.perform(post("/api/oeuvres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(oeuvre)))
                .andExpect(status().isBadRequest());

        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEditeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = oeuvreRepository.findAll().size();
        // set the field null
        oeuvre.setEditeur(null);

        // Create the Oeuvre, which fails.

        restOeuvreMockMvc.perform(post("/api/oeuvres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(oeuvre)))
                .andExpect(status().isBadRequest());

        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = oeuvreRepository.findAll().size();
        // set the field null
        oeuvre.setDescription(null);

        // Create the Oeuvre, which fails.

        restOeuvreMockMvc.perform(post("/api/oeuvres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(oeuvre)))
                .andExpect(status().isBadRequest());

        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstLivreIsRequired() throws Exception {
        int databaseSizeBeforeTest = oeuvreRepository.findAll().size();
        // set the field null
        oeuvre.setEstLivre(null);

        // Create the Oeuvre, which fails.

        restOeuvreMockMvc.perform(post("/api/oeuvres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(oeuvre)))
                .andExpect(status().isBadRequest());

        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOeuvres() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);

        // Get all the oeuvres
        restOeuvreMockMvc.perform(get("/api/oeuvres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(oeuvre.getId().intValue())))
                .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
                .andExpect(jsonPath("$.[*].editeur").value(hasItem(DEFAULT_EDITEUR.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].dataPublication").value(hasItem(DEFAULT_DATA_PUBLICATION.toString())))
                .andExpect(jsonPath("$.[*].dateEdition").value(hasItem(DEFAULT_DATE_EDITION.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
                .andExpect(jsonPath("$.[*].parution").value(hasItem(DEFAULT_PARUTION.toString())))
                .andExpect(jsonPath("$.[*].periodicite").value(hasItem(DEFAULT_PERIODICITE)))
                .andExpect(jsonPath("$.[*].estLivre").value(hasItem(DEFAULT_EST_LIVRE.booleanValue())));
    }

    @Test
    @Transactional
    public void getOeuvre() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);

        // Get the oeuvre
        restOeuvreMockMvc.perform(get("/api/oeuvres/{id}", oeuvre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(oeuvre.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.editeur").value(DEFAULT_EDITEUR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dataPublication").value(DEFAULT_DATA_PUBLICATION.toString()))
            .andExpect(jsonPath("$.dateEdition").value(DEFAULT_DATE_EDITION.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.parution").value(DEFAULT_PARUTION.toString()))
            .andExpect(jsonPath("$.periodicite").value(DEFAULT_PERIODICITE))
            .andExpect(jsonPath("$.estLivre").value(DEFAULT_EST_LIVRE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOeuvre() throws Exception {
        // Get the oeuvre
        restOeuvreMockMvc.perform(get("/api/oeuvres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOeuvre() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);
        int databaseSizeBeforeUpdate = oeuvreRepository.findAll().size();

        // Update the oeuvre
        Oeuvre updatedOeuvre = oeuvreRepository.findOne(oeuvre.getId());
        updatedOeuvre
                .titre(UPDATED_TITRE)
                .editeur(UPDATED_EDITEUR)
                .description(UPDATED_DESCRIPTION)
                .dataPublication(UPDATED_DATA_PUBLICATION)
                .dateEdition(UPDATED_DATE_EDITION)
                .numero(UPDATED_NUMERO)
                .parution(UPDATED_PARUTION)
                .periodicite(UPDATED_PERIODICITE)
                .estLivre(UPDATED_EST_LIVRE);

        restOeuvreMockMvc.perform(put("/api/oeuvres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOeuvre)))
                .andExpect(status().isOk());

        // Validate the Oeuvre in the database
        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeUpdate);
        Oeuvre testOeuvre = oeuvres.get(oeuvres.size() - 1);
        assertThat(testOeuvre.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testOeuvre.getEditeur()).isEqualTo(UPDATED_EDITEUR);
        assertThat(testOeuvre.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOeuvre.getDataPublication()).isEqualTo(UPDATED_DATA_PUBLICATION);
        assertThat(testOeuvre.getDateEdition()).isEqualTo(UPDATED_DATE_EDITION);
        assertThat(testOeuvre.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testOeuvre.getParution()).isEqualTo(UPDATED_PARUTION);
        assertThat(testOeuvre.getPeriodicite()).isEqualTo(UPDATED_PERIODICITE);
        assertThat(testOeuvre.isEstLivre()).isEqualTo(UPDATED_EST_LIVRE);
    }

    @Test
    @Transactional
    public void deleteOeuvre() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);
        int databaseSizeBeforeDelete = oeuvreRepository.findAll().size();

        // Get the oeuvre
        restOeuvreMockMvc.perform(delete("/api/oeuvres/{id}", oeuvre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        assertThat(oeuvres).hasSize(databaseSizeBeforeDelete - 1);
    }
}

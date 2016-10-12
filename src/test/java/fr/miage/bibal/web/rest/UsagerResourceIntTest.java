package fr.miage.bibal.web.rest;

import fr.miage.bibal.BibalApp;

import fr.miage.bibal.domain.Usager;
import fr.miage.bibal.repository.UsagerRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UsagerResource REST controller.
 *
 * @see UsagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BibalApp.class)
public class UsagerResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";

    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_NAISSANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_NAISSANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_NAISSANCE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE_NAISSANCE);

    private static final String DEFAULT_ADRESSE = "AAAAA";
    private static final String UPDATED_ADRESSE = "BBBBB";

    @Inject
    private UsagerRepository usagerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUsagerMockMvc;

    private Usager usager;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsagerResource usagerResource = new UsagerResource();
        ReflectionTestUtils.setField(usagerResource, "usagerRepository", usagerRepository);
        this.restUsagerMockMvc = MockMvcBuilders.standaloneSetup(usagerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usager createEntity(EntityManager em) {
        Usager usager = new Usager()
                .nom(DEFAULT_NOM)
                .prenom(DEFAULT_PRENOM)
                .dateNaissance(DEFAULT_DATE_NAISSANCE)
                .adresse(DEFAULT_ADRESSE);
        return usager;
    }

    @Before
    public void initTest() {
        usager = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsager() throws Exception {
        int databaseSizeBeforeCreate = usagerRepository.findAll().size();

        // Create the Usager

        restUsagerMockMvc.perform(post("/api/usagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usager)))
                .andExpect(status().isCreated());

        // Validate the Usager in the database
        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeCreate + 1);
        Usager testUsager = usagers.get(usagers.size() - 1);
        assertThat(testUsager.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUsager.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUsager.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testUsager.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = usagerRepository.findAll().size();
        // set the field null
        usager.setNom(null);

        // Create the Usager, which fails.

        restUsagerMockMvc.perform(post("/api/usagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usager)))
                .andExpect(status().isBadRequest());

        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = usagerRepository.findAll().size();
        // set the field null
        usager.setPrenom(null);

        // Create the Usager, which fails.

        restUsagerMockMvc.perform(post("/api/usagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usager)))
                .andExpect(status().isBadRequest());

        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = usagerRepository.findAll().size();
        // set the field null
        usager.setDateNaissance(null);

        // Create the Usager, which fails.

        restUsagerMockMvc.perform(post("/api/usagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usager)))
                .andExpect(status().isBadRequest());

        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = usagerRepository.findAll().size();
        // set the field null
        usager.setAdresse(null);

        // Create the Usager, which fails.

        restUsagerMockMvc.perform(post("/api/usagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usager)))
                .andExpect(status().isBadRequest());

        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUsagers() throws Exception {
        // Initialize the database
        usagerRepository.saveAndFlush(usager);

        // Get all the usagers
        restUsagerMockMvc.perform(get("/api/usagers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(usager.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE_STR)))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }

    @Test
    @Transactional
    public void getUsager() throws Exception {
        // Initialize the database
        usagerRepository.saveAndFlush(usager);

        // Get the usager
        restUsagerMockMvc.perform(get("/api/usagers/{id}", usager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usager.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE_STR))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUsager() throws Exception {
        // Get the usager
        restUsagerMockMvc.perform(get("/api/usagers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsager() throws Exception {
        // Initialize the database
        usagerRepository.saveAndFlush(usager);
        int databaseSizeBeforeUpdate = usagerRepository.findAll().size();

        // Update the usager
        Usager updatedUsager = usagerRepository.findOne(usager.getId());
        updatedUsager
                .nom(UPDATED_NOM)
                .prenom(UPDATED_PRENOM)
                .dateNaissance(UPDATED_DATE_NAISSANCE)
                .adresse(UPDATED_ADRESSE);

        restUsagerMockMvc.perform(put("/api/usagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUsager)))
                .andExpect(status().isOk());

        // Validate the Usager in the database
        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeUpdate);
        Usager testUsager = usagers.get(usagers.size() - 1);
        assertThat(testUsager.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUsager.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUsager.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testUsager.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void deleteUsager() throws Exception {
        // Initialize the database
        usagerRepository.saveAndFlush(usager);
        int databaseSizeBeforeDelete = usagerRepository.findAll().size();

        // Get the usager
        restUsagerMockMvc.perform(delete("/api/usagers/{id}", usager.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Usager> usagers = usagerRepository.findAll();
        assertThat(usagers).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package fr.miage.bibal.web.rest;

import fr.miage.bibal.BibalApp;

import fr.miage.bibal.domain.Emprunt;
import fr.miage.bibal.domain.Usager;
import fr.miage.bibal.domain.Exemplaire;
import fr.miage.bibal.repository.EmpruntRepository;

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
 * Test class for the EmpruntResource REST controller.
 *
 * @see EmpruntResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BibalApp.class)
public class EmpruntResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_EMPRUNT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_EMPRUNT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_EMPRUNT_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE_EMPRUNT);

    private static final ZonedDateTime DEFAULT_DATE_RETOUR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_RETOUR = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_RETOUR_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE_RETOUR);

    @Inject
    private EmpruntRepository empruntRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEmpruntMockMvc;

    private Emprunt emprunt;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmpruntResource empruntResource = new EmpruntResource();
        ReflectionTestUtils.setField(empruntResource, "empruntRepository", empruntRepository);
        this.restEmpruntMockMvc = MockMvcBuilders.standaloneSetup(empruntResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emprunt createEntity(EntityManager em) {
        Emprunt emprunt = new Emprunt()
                .dateEmprunt(DEFAULT_DATE_EMPRUNT)
                .dateRetour(DEFAULT_DATE_RETOUR);
        // Add required entity
        Usager usager = UsagerResourceIntTest.createEntity(em);
        em.persist(usager);
        em.flush();
        emprunt.setUsager(usager);
        // Add required entity
        Exemplaire exemplaire = ExemplaireResourceIntTest.createEntity(em);
        em.persist(exemplaire);
        em.flush();
        emprunt.setExemplaire(exemplaire);
        return emprunt;
    }

    @Before
    public void initTest() {
        emprunt = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmprunt() throws Exception {
        int databaseSizeBeforeCreate = empruntRepository.findAll().size();

        // Create the Emprunt

        restEmpruntMockMvc.perform(post("/api/emprunts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emprunt)))
                .andExpect(status().isCreated());

        // Validate the Emprunt in the database
        List<Emprunt> emprunts = empruntRepository.findAll();
        assertThat(emprunts).hasSize(databaseSizeBeforeCreate + 1);
        Emprunt testEmprunt = emprunts.get(emprunts.size() - 1);
        assertThat(testEmprunt.getDateEmprunt()).isEqualTo(DEFAULT_DATE_EMPRUNT);
        assertThat(testEmprunt.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
    }

    @Test
    @Transactional
    public void checkDateEmpruntIsRequired() throws Exception {
        int databaseSizeBeforeTest = empruntRepository.findAll().size();
        // set the field null
        emprunt.setDateEmprunt(null);

        // Create the Emprunt, which fails.

        restEmpruntMockMvc.perform(post("/api/emprunts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emprunt)))
                .andExpect(status().isBadRequest());

        List<Emprunt> emprunts = empruntRepository.findAll();
        assertThat(emprunts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmprunts() throws Exception {
        // Initialize the database
        empruntRepository.saveAndFlush(emprunt);

        // Get all the emprunts
        restEmpruntMockMvc.perform(get("/api/emprunts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(emprunt.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateEmprunt").value(hasItem(DEFAULT_DATE_EMPRUNT_STR)))
                .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR_STR)));
    }

    @Test
    @Transactional
    public void getEmprunt() throws Exception {
        // Initialize the database
        empruntRepository.saveAndFlush(emprunt);

        // Get the emprunt
        restEmpruntMockMvc.perform(get("/api/emprunts/{id}", emprunt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emprunt.getId().intValue()))
            .andExpect(jsonPath("$.dateEmprunt").value(DEFAULT_DATE_EMPRUNT_STR))
            .andExpect(jsonPath("$.dateRetour").value(DEFAULT_DATE_RETOUR_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEmprunt() throws Exception {
        // Get the emprunt
        restEmpruntMockMvc.perform(get("/api/emprunts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmprunt() throws Exception {
        // Initialize the database
        empruntRepository.saveAndFlush(emprunt);
        int databaseSizeBeforeUpdate = empruntRepository.findAll().size();

        // Update the emprunt
        Emprunt updatedEmprunt = empruntRepository.findOne(emprunt.getId());
        updatedEmprunt
                .dateEmprunt(UPDATED_DATE_EMPRUNT)
                .dateRetour(UPDATED_DATE_RETOUR);

        restEmpruntMockMvc.perform(put("/api/emprunts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmprunt)))
                .andExpect(status().isOk());

        // Validate the Emprunt in the database
        List<Emprunt> emprunts = empruntRepository.findAll();
        assertThat(emprunts).hasSize(databaseSizeBeforeUpdate);
        Emprunt testEmprunt = emprunts.get(emprunts.size() - 1);
        assertThat(testEmprunt.getDateEmprunt()).isEqualTo(UPDATED_DATE_EMPRUNT);
        assertThat(testEmprunt.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
    }

    @Test
    @Transactional
    public void deleteEmprunt() throws Exception {
        // Initialize the database
        empruntRepository.saveAndFlush(emprunt);
        int databaseSizeBeforeDelete = empruntRepository.findAll().size();

        // Get the emprunt
        restEmpruntMockMvc.perform(delete("/api/emprunts/{id}", emprunt.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Emprunt> emprunts = empruntRepository.findAll();
        assertThat(emprunts).hasSize(databaseSizeBeforeDelete - 1);
    }
}

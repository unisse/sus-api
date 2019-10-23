package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.Importancia;
import org.unisse.sus.domain.Registro;
import org.unisse.sus.repository.ImportanciaRepository;
import org.unisse.sus.service.ImportanciaService;
import org.unisse.sus.service.dto.ImportanciaDTO;
import org.unisse.sus.service.mapper.ImportanciaMapper;
import org.unisse.sus.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.unisse.sus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ImportanciaResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class ImportanciaResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ImportanciaRepository importanciaRepository;

    @Autowired
    private ImportanciaMapper importanciaMapper;

    @Autowired
    private ImportanciaService importanciaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restImportanciaMockMvc;

    private Importancia importancia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ImportanciaResource importanciaResource = new ImportanciaResource(importanciaService);
        this.restImportanciaMockMvc = MockMvcBuilders.standaloneSetup(importanciaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Importancia createEntity(EntityManager em) {
        Importancia importancia = new Importancia()
            .criacao(DEFAULT_CRIACAO);
        // Add required entity
        Registro registro;
        if (TestUtil.findAll(em, Registro.class).isEmpty()) {
            registro = RegistroResourceIT.createEntity(em);
            em.persist(registro);
            em.flush();
        } else {
            registro = TestUtil.findAll(em, Registro.class).get(0);
        }
        importancia.setRegistro(registro);
        return importancia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Importancia createUpdatedEntity(EntityManager em) {
        Importancia importancia = new Importancia()
            .criacao(UPDATED_CRIACAO);
        // Add required entity
        Registro registro;
        if (TestUtil.findAll(em, Registro.class).isEmpty()) {
            registro = RegistroResourceIT.createUpdatedEntity(em);
            em.persist(registro);
            em.flush();
        } else {
            registro = TestUtil.findAll(em, Registro.class).get(0);
        }
        importancia.setRegistro(registro);
        return importancia;
    }

    @BeforeEach
    public void initTest() {
        importancia = createEntity(em);
    }

    @Test
    @Transactional
    public void createImportancia() throws Exception {
        int databaseSizeBeforeCreate = importanciaRepository.findAll().size();

        // Create the Importancia
        ImportanciaDTO importanciaDTO = importanciaMapper.toDto(importancia);
        restImportanciaMockMvc.perform(post("/api/importancias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importanciaDTO)))
            .andExpect(status().isCreated());

        // Validate the Importancia in the database
        List<Importancia> importanciaList = importanciaRepository.findAll();
        assertThat(importanciaList).hasSize(databaseSizeBeforeCreate + 1);
        Importancia testImportancia = importanciaList.get(importanciaList.size() - 1);
        assertThat(testImportancia.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
    }

    @Test
    @Transactional
    public void createImportanciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = importanciaRepository.findAll().size();

        // Create the Importancia with an existing ID
        importancia.setId(1L);
        ImportanciaDTO importanciaDTO = importanciaMapper.toDto(importancia);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImportanciaMockMvc.perform(post("/api/importancias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importanciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Importancia in the database
        List<Importancia> importanciaList = importanciaRepository.findAll();
        assertThat(importanciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = importanciaRepository.findAll().size();
        // set the field null
        importancia.setCriacao(null);

        // Create the Importancia, which fails.
        ImportanciaDTO importanciaDTO = importanciaMapper.toDto(importancia);

        restImportanciaMockMvc.perform(post("/api/importancias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importanciaDTO)))
            .andExpect(status().isBadRequest());

        List<Importancia> importanciaList = importanciaRepository.findAll();
        assertThat(importanciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImportancias() throws Exception {
        // Initialize the database
        importanciaRepository.saveAndFlush(importancia);

        // Get all the importanciaList
        restImportanciaMockMvc.perform(get("/api/importancias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(importancia.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())));
    }
    
    @Test
    @Transactional
    public void getImportancia() throws Exception {
        // Initialize the database
        importanciaRepository.saveAndFlush(importancia);

        // Get the importancia
        restImportanciaMockMvc.perform(get("/api/importancias/{id}", importancia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(importancia.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingImportancia() throws Exception {
        // Get the importancia
        restImportanciaMockMvc.perform(get("/api/importancias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImportancia() throws Exception {
        // Initialize the database
        importanciaRepository.saveAndFlush(importancia);

        int databaseSizeBeforeUpdate = importanciaRepository.findAll().size();

        // Update the importancia
        Importancia updatedImportancia = importanciaRepository.findById(importancia.getId()).get();
        // Disconnect from session so that the updates on updatedImportancia are not directly saved in db
        em.detach(updatedImportancia);
        updatedImportancia
            .criacao(UPDATED_CRIACAO);
        ImportanciaDTO importanciaDTO = importanciaMapper.toDto(updatedImportancia);

        restImportanciaMockMvc.perform(put("/api/importancias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importanciaDTO)))
            .andExpect(status().isOk());

        // Validate the Importancia in the database
        List<Importancia> importanciaList = importanciaRepository.findAll();
        assertThat(importanciaList).hasSize(databaseSizeBeforeUpdate);
        Importancia testImportancia = importanciaList.get(importanciaList.size() - 1);
        assertThat(testImportancia.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingImportancia() throws Exception {
        int databaseSizeBeforeUpdate = importanciaRepository.findAll().size();

        // Create the Importancia
        ImportanciaDTO importanciaDTO = importanciaMapper.toDto(importancia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImportanciaMockMvc.perform(put("/api/importancias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importanciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Importancia in the database
        List<Importancia> importanciaList = importanciaRepository.findAll();
        assertThat(importanciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteImportancia() throws Exception {
        // Initialize the database
        importanciaRepository.saveAndFlush(importancia);

        int databaseSizeBeforeDelete = importanciaRepository.findAll().size();

        // Delete the importancia
        restImportanciaMockMvc.perform(delete("/api/importancias/{id}", importancia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Importancia> importanciaList = importanciaRepository.findAll();
        assertThat(importanciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Importancia.class);
        Importancia importancia1 = new Importancia();
        importancia1.setId(1L);
        Importancia importancia2 = new Importancia();
        importancia2.setId(importancia1.getId());
        assertThat(importancia1).isEqualTo(importancia2);
        importancia2.setId(2L);
        assertThat(importancia1).isNotEqualTo(importancia2);
        importancia1.setId(null);
        assertThat(importancia1).isNotEqualTo(importancia2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImportanciaDTO.class);
        ImportanciaDTO importanciaDTO1 = new ImportanciaDTO();
        importanciaDTO1.setId(1L);
        ImportanciaDTO importanciaDTO2 = new ImportanciaDTO();
        assertThat(importanciaDTO1).isNotEqualTo(importanciaDTO2);
        importanciaDTO2.setId(importanciaDTO1.getId());
        assertThat(importanciaDTO1).isEqualTo(importanciaDTO2);
        importanciaDTO2.setId(2L);
        assertThat(importanciaDTO1).isNotEqualTo(importanciaDTO2);
        importanciaDTO1.setId(null);
        assertThat(importanciaDTO1).isNotEqualTo(importanciaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(importanciaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(importanciaMapper.fromId(null)).isNull();
    }
}

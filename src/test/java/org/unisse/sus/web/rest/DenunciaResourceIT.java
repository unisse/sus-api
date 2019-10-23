package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.Denuncia;
import org.unisse.sus.domain.Registro;
import org.unisse.sus.domain.TipoDenuncia;
import org.unisse.sus.repository.DenunciaRepository;
import org.unisse.sus.service.DenunciaService;
import org.unisse.sus.service.dto.DenunciaDTO;
import org.unisse.sus.service.mapper.DenunciaMapper;
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
 * Integration tests for the {@link DenunciaResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class DenunciaResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private DenunciaMapper denunciaMapper;

    @Autowired
    private DenunciaService denunciaService;

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

    private MockMvc restDenunciaMockMvc;

    private Denuncia denuncia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DenunciaResource denunciaResource = new DenunciaResource(denunciaService);
        this.restDenunciaMockMvc = MockMvcBuilders.standaloneSetup(denunciaResource)
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
    public static Denuncia createEntity(EntityManager em) {
        Denuncia denuncia = new Denuncia()
            .criacao(DEFAULT_CRIACAO)
            .descricao(DEFAULT_DESCRICAO);
        // Add required entity
        Registro registro;
        if (TestUtil.findAll(em, Registro.class).isEmpty()) {
            registro = RegistroResourceIT.createEntity(em);
            em.persist(registro);
            em.flush();
        } else {
            registro = TestUtil.findAll(em, Registro.class).get(0);
        }
        denuncia.setRegistro(registro);
        // Add required entity
        TipoDenuncia tipoDenuncia;
        if (TestUtil.findAll(em, TipoDenuncia.class).isEmpty()) {
            tipoDenuncia = TipoDenunciaResourceIT.createEntity(em);
            em.persist(tipoDenuncia);
            em.flush();
        } else {
            tipoDenuncia = TestUtil.findAll(em, TipoDenuncia.class).get(0);
        }
        denuncia.setTipo(tipoDenuncia);
        return denuncia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Denuncia createUpdatedEntity(EntityManager em) {
        Denuncia denuncia = new Denuncia()
            .criacao(UPDATED_CRIACAO)
            .descricao(UPDATED_DESCRICAO);
        // Add required entity
        Registro registro;
        if (TestUtil.findAll(em, Registro.class).isEmpty()) {
            registro = RegistroResourceIT.createUpdatedEntity(em);
            em.persist(registro);
            em.flush();
        } else {
            registro = TestUtil.findAll(em, Registro.class).get(0);
        }
        denuncia.setRegistro(registro);
        // Add required entity
        TipoDenuncia tipoDenuncia;
        if (TestUtil.findAll(em, TipoDenuncia.class).isEmpty()) {
            tipoDenuncia = TipoDenunciaResourceIT.createUpdatedEntity(em);
            em.persist(tipoDenuncia);
            em.flush();
        } else {
            tipoDenuncia = TestUtil.findAll(em, TipoDenuncia.class).get(0);
        }
        denuncia.setTipo(tipoDenuncia);
        return denuncia;
    }

    @BeforeEach
    public void initTest() {
        denuncia = createEntity(em);
    }

    @Test
    @Transactional
    public void createDenuncia() throws Exception {
        int databaseSizeBeforeCreate = denunciaRepository.findAll().size();

        // Create the Denuncia
        DenunciaDTO denunciaDTO = denunciaMapper.toDto(denuncia);
        restDenunciaMockMvc.perform(post("/api/denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(denunciaDTO)))
            .andExpect(status().isCreated());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeCreate + 1);
        Denuncia testDenuncia = denunciaList.get(denunciaList.size() - 1);
        assertThat(testDenuncia.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
        assertThat(testDenuncia.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createDenunciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = denunciaRepository.findAll().size();

        // Create the Denuncia with an existing ID
        denuncia.setId(1L);
        DenunciaDTO denunciaDTO = denunciaMapper.toDto(denuncia);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDenunciaMockMvc.perform(post("/api/denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(denunciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = denunciaRepository.findAll().size();
        // set the field null
        denuncia.setCriacao(null);

        // Create the Denuncia, which fails.
        DenunciaDTO denunciaDTO = denunciaMapper.toDto(denuncia);

        restDenunciaMockMvc.perform(post("/api/denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(denunciaDTO)))
            .andExpect(status().isBadRequest());

        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDenuncias() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        // Get all the denunciaList
        restDenunciaMockMvc.perform(get("/api/denuncias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(denuncia.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        // Get the denuncia
        restDenunciaMockMvc.perform(get("/api/denuncias/{id}", denuncia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(denuncia.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingDenuncia() throws Exception {
        // Get the denuncia
        restDenunciaMockMvc.perform(get("/api/denuncias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();

        // Update the denuncia
        Denuncia updatedDenuncia = denunciaRepository.findById(denuncia.getId()).get();
        // Disconnect from session so that the updates on updatedDenuncia are not directly saved in db
        em.detach(updatedDenuncia);
        updatedDenuncia
            .criacao(UPDATED_CRIACAO)
            .descricao(UPDATED_DESCRICAO);
        DenunciaDTO denunciaDTO = denunciaMapper.toDto(updatedDenuncia);

        restDenunciaMockMvc.perform(put("/api/denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(denunciaDTO)))
            .andExpect(status().isOk());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
        Denuncia testDenuncia = denunciaList.get(denunciaList.size() - 1);
        assertThat(testDenuncia.getCriacao()).isEqualTo(UPDATED_CRIACAO);
        assertThat(testDenuncia.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();

        // Create the Denuncia
        DenunciaDTO denunciaDTO = denunciaMapper.toDto(denuncia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDenunciaMockMvc.perform(put("/api/denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(denunciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        int databaseSizeBeforeDelete = denunciaRepository.findAll().size();

        // Delete the denuncia
        restDenunciaMockMvc.perform(delete("/api/denuncias/{id}", denuncia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Denuncia.class);
        Denuncia denuncia1 = new Denuncia();
        denuncia1.setId(1L);
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setId(denuncia1.getId());
        assertThat(denuncia1).isEqualTo(denuncia2);
        denuncia2.setId(2L);
        assertThat(denuncia1).isNotEqualTo(denuncia2);
        denuncia1.setId(null);
        assertThat(denuncia1).isNotEqualTo(denuncia2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DenunciaDTO.class);
        DenunciaDTO denunciaDTO1 = new DenunciaDTO();
        denunciaDTO1.setId(1L);
        DenunciaDTO denunciaDTO2 = new DenunciaDTO();
        assertThat(denunciaDTO1).isNotEqualTo(denunciaDTO2);
        denunciaDTO2.setId(denunciaDTO1.getId());
        assertThat(denunciaDTO1).isEqualTo(denunciaDTO2);
        denunciaDTO2.setId(2L);
        assertThat(denunciaDTO1).isNotEqualTo(denunciaDTO2);
        denunciaDTO1.setId(null);
        assertThat(denunciaDTO1).isNotEqualTo(denunciaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(denunciaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(denunciaMapper.fromId(null)).isNull();
    }
}

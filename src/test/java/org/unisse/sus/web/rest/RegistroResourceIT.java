package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.Registro;
import org.unisse.sus.domain.TipoRegistro;
import org.unisse.sus.domain.UnidadeSaude;
import org.unisse.sus.repository.RegistroRepository;
import org.unisse.sus.service.RegistroService;
import org.unisse.sus.service.dto.RegistroDTO;
import org.unisse.sus.service.mapper.RegistroMapper;
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
 * Integration tests for the {@link RegistroResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class RegistroResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private RegistroMapper registroMapper;

    @Autowired
    private RegistroService registroService;

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

    private MockMvc restRegistroMockMvc;

    private Registro registro;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegistroResource registroResource = new RegistroResource(registroService);
        this.restRegistroMockMvc = MockMvcBuilders.standaloneSetup(registroResource)
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
    public static Registro createEntity(EntityManager em) {
        Registro registro = new Registro()
            .criacao(DEFAULT_CRIACAO)
            .descricao(DEFAULT_DESCRICAO);
        // Add required entity
        TipoRegistro tipoRegistro;
        if (TestUtil.findAll(em, TipoRegistro.class).isEmpty()) {
            tipoRegistro = TipoRegistroResourceIT.createEntity(em);
            em.persist(tipoRegistro);
            em.flush();
        } else {
            tipoRegistro = TestUtil.findAll(em, TipoRegistro.class).get(0);
        }
        registro.setTipo(tipoRegistro);
        // Add required entity
        UnidadeSaude unidadeSaude;
        if (TestUtil.findAll(em, UnidadeSaude.class).isEmpty()) {
            unidadeSaude = UnidadeSaudeResourceIT.createEntity(em);
            em.persist(unidadeSaude);
            em.flush();
        } else {
            unidadeSaude = TestUtil.findAll(em, UnidadeSaude.class).get(0);
        }
        registro.setUnidade(unidadeSaude);
        return registro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registro createUpdatedEntity(EntityManager em) {
        Registro registro = new Registro()
            .criacao(UPDATED_CRIACAO)
            .descricao(UPDATED_DESCRICAO);
        // Add required entity
        TipoRegistro tipoRegistro;
        if (TestUtil.findAll(em, TipoRegistro.class).isEmpty()) {
            tipoRegistro = TipoRegistroResourceIT.createUpdatedEntity(em);
            em.persist(tipoRegistro);
            em.flush();
        } else {
            tipoRegistro = TestUtil.findAll(em, TipoRegistro.class).get(0);
        }
        registro.setTipo(tipoRegistro);
        // Add required entity
        UnidadeSaude unidadeSaude;
        if (TestUtil.findAll(em, UnidadeSaude.class).isEmpty()) {
            unidadeSaude = UnidadeSaudeResourceIT.createUpdatedEntity(em);
            em.persist(unidadeSaude);
            em.flush();
        } else {
            unidadeSaude = TestUtil.findAll(em, UnidadeSaude.class).get(0);
        }
        registro.setUnidade(unidadeSaude);
        return registro;
    }

    @BeforeEach
    public void initTest() {
        registro = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistro() throws Exception {
        int databaseSizeBeforeCreate = registroRepository.findAll().size();

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);
        restRegistroMockMvc.perform(post("/api/registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isCreated());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeCreate + 1);
        Registro testRegistro = registroList.get(registroList.size() - 1);
        assertThat(testRegistro.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
        assertThat(testRegistro.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createRegistroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registroRepository.findAll().size();

        // Create the Registro with an existing ID
        registro.setId(1L);
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistroMockMvc.perform(post("/api/registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = registroRepository.findAll().size();
        // set the field null
        registro.setCriacao(null);

        // Create the Registro, which fails.
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        restRegistroMockMvc.perform(post("/api/registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isBadRequest());

        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegistros() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList
        restRegistroMockMvc.perform(get("/api/registros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registro.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getRegistro() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get the registro
        restRegistroMockMvc.perform(get("/api/registros/{id}", registro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registro.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingRegistro() throws Exception {
        // Get the registro
        restRegistroMockMvc.perform(get("/api/registros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistro() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        int databaseSizeBeforeUpdate = registroRepository.findAll().size();

        // Update the registro
        Registro updatedRegistro = registroRepository.findById(registro.getId()).get();
        // Disconnect from session so that the updates on updatedRegistro are not directly saved in db
        em.detach(updatedRegistro);
        updatedRegistro
            .criacao(UPDATED_CRIACAO)
            .descricao(UPDATED_DESCRICAO);
        RegistroDTO registroDTO = registroMapper.toDto(updatedRegistro);

        restRegistroMockMvc.perform(put("/api/registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isOk());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
        Registro testRegistro = registroList.get(registroList.size() - 1);
        assertThat(testRegistro.getCriacao()).isEqualTo(UPDATED_CRIACAO);
        assertThat(testRegistro.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroMockMvc.perform(put("/api/registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegistro() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        int databaseSizeBeforeDelete = registroRepository.findAll().size();

        // Delete the registro
        restRegistroMockMvc.perform(delete("/api/registros/{id}", registro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registro.class);
        Registro registro1 = new Registro();
        registro1.setId(1L);
        Registro registro2 = new Registro();
        registro2.setId(registro1.getId());
        assertThat(registro1).isEqualTo(registro2);
        registro2.setId(2L);
        assertThat(registro1).isNotEqualTo(registro2);
        registro1.setId(null);
        assertThat(registro1).isNotEqualTo(registro2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroDTO.class);
        RegistroDTO registroDTO1 = new RegistroDTO();
        registroDTO1.setId(1L);
        RegistroDTO registroDTO2 = new RegistroDTO();
        assertThat(registroDTO1).isNotEqualTo(registroDTO2);
        registroDTO2.setId(registroDTO1.getId());
        assertThat(registroDTO1).isEqualTo(registroDTO2);
        registroDTO2.setId(2L);
        assertThat(registroDTO1).isNotEqualTo(registroDTO2);
        registroDTO1.setId(null);
        assertThat(registroDTO1).isNotEqualTo(registroDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(registroMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(registroMapper.fromId(null)).isNull();
    }
}

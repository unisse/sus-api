package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.UnidadeSaude;
import org.unisse.sus.domain.TipoUnidadeSaude;
import org.unisse.sus.domain.SituacaoUnidadeSaude;
import org.unisse.sus.repository.UnidadeSaudeRepository;
import org.unisse.sus.service.UnidadeSaudeService;
import org.unisse.sus.service.dto.UnidadeSaudeDTO;
import org.unisse.sus.service.mapper.UnidadeSaudeMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static org.unisse.sus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UnidadeSaudeResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class UnidadeSaudeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_HORARIO = "AAAAAAAAAA";
    private static final String UPDATED_HORARIO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_UF = "AAAAAAAAAA";
    private static final String UPDATED_UF = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    @Autowired
    private UnidadeSaudeRepository unidadeSaudeRepository;

    @Autowired
    private UnidadeSaudeMapper unidadeSaudeMapper;

    @Autowired
    private UnidadeSaudeService unidadeSaudeService;

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

    private MockMvc restUnidadeSaudeMockMvc;

    private UnidadeSaude unidadeSaude;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnidadeSaudeResource unidadeSaudeResource = new UnidadeSaudeResource(unidadeSaudeService);
        this.restUnidadeSaudeMockMvc = MockMvcBuilders.standaloneSetup(unidadeSaudeResource)
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
    public static UnidadeSaude createEntity(EntityManager em) {
        UnidadeSaude unidadeSaude = new UnidadeSaude()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .horario(DEFAULT_HORARIO)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .uf(DEFAULT_UF)
            .cep(DEFAULT_CEP)
            .referencia(DEFAULT_REFERENCIA);
        // Add required entity
        TipoUnidadeSaude tipoUnidadeSaude;
        if (TestUtil.findAll(em, TipoUnidadeSaude.class).isEmpty()) {
            tipoUnidadeSaude = TipoUnidadeSaudeResourceIT.createEntity(em);
            em.persist(tipoUnidadeSaude);
            em.flush();
        } else {
            tipoUnidadeSaude = TestUtil.findAll(em, TipoUnidadeSaude.class).get(0);
        }
        unidadeSaude.setTipo(tipoUnidadeSaude);
        // Add required entity
        SituacaoUnidadeSaude situacaoUnidadeSaude;
        if (TestUtil.findAll(em, SituacaoUnidadeSaude.class).isEmpty()) {
            situacaoUnidadeSaude = SituacaoUnidadeSaudeResourceIT.createEntity(em);
            em.persist(situacaoUnidadeSaude);
            em.flush();
        } else {
            situacaoUnidadeSaude = TestUtil.findAll(em, SituacaoUnidadeSaude.class).get(0);
        }
        unidadeSaude.setSituacao(situacaoUnidadeSaude);
        return unidadeSaude;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnidadeSaude createUpdatedEntity(EntityManager em) {
        UnidadeSaude unidadeSaude = new UnidadeSaude()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .horario(UPDATED_HORARIO)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .uf(UPDATED_UF)
            .cep(UPDATED_CEP)
            .referencia(UPDATED_REFERENCIA);
        // Add required entity
        TipoUnidadeSaude tipoUnidadeSaude;
        if (TestUtil.findAll(em, TipoUnidadeSaude.class).isEmpty()) {
            tipoUnidadeSaude = TipoUnidadeSaudeResourceIT.createUpdatedEntity(em);
            em.persist(tipoUnidadeSaude);
            em.flush();
        } else {
            tipoUnidadeSaude = TestUtil.findAll(em, TipoUnidadeSaude.class).get(0);
        }
        unidadeSaude.setTipo(tipoUnidadeSaude);
        // Add required entity
        SituacaoUnidadeSaude situacaoUnidadeSaude;
        if (TestUtil.findAll(em, SituacaoUnidadeSaude.class).isEmpty()) {
            situacaoUnidadeSaude = SituacaoUnidadeSaudeResourceIT.createUpdatedEntity(em);
            em.persist(situacaoUnidadeSaude);
            em.flush();
        } else {
            situacaoUnidadeSaude = TestUtil.findAll(em, SituacaoUnidadeSaude.class).get(0);
        }
        unidadeSaude.setSituacao(situacaoUnidadeSaude);
        return unidadeSaude;
    }

    @BeforeEach
    public void initTest() {
        unidadeSaude = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnidadeSaude() throws Exception {
        int databaseSizeBeforeCreate = unidadeSaudeRepository.findAll().size();

        // Create the UnidadeSaude
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);
        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isCreated());

        // Validate the UnidadeSaude in the database
        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeCreate + 1);
        UnidadeSaude testUnidadeSaude = unidadeSaudeList.get(unidadeSaudeList.size() - 1);
        assertThat(testUnidadeSaude.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testUnidadeSaude.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testUnidadeSaude.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testUnidadeSaude.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testUnidadeSaude.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testUnidadeSaude.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testUnidadeSaude.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testUnidadeSaude.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testUnidadeSaude.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testUnidadeSaude.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testUnidadeSaude.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
    }

    @Test
    @Transactional
    public void createUnidadeSaudeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unidadeSaudeRepository.findAll().size();

        // Create the UnidadeSaude with an existing ID
        unidadeSaude.setId(1L);
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UnidadeSaude in the database
        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setNome(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setDescricao(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHorarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setHorario(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setLatitude(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setLongitude(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnderecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setEndereco(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBairroIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setBairro(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setCidade(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUfIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setUf(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setCep(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReferenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeSaudeRepository.findAll().size();
        // set the field null
        unidadeSaude.setReferencia(null);

        // Create the UnidadeSaude, which fails.
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        restUnidadeSaudeMockMvc.perform(post("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnidadeSaudes() throws Exception {
        // Initialize the database
        unidadeSaudeRepository.saveAndFlush(unidadeSaude);

        // Get all the unidadeSaudeList
        restUnidadeSaudeMockMvc.perform(get("/api/unidade-saudes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unidadeSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)));
    }
    
    @Test
    @Transactional
    public void getUnidadeSaude() throws Exception {
        // Initialize the database
        unidadeSaudeRepository.saveAndFlush(unidadeSaude);

        // Get the unidadeSaude
        restUnidadeSaudeMockMvc.perform(get("/api/unidade-saudes/{id}", unidadeSaude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unidadeSaude.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA));
    }

    @Test
    @Transactional
    public void getNonExistingUnidadeSaude() throws Exception {
        // Get the unidadeSaude
        restUnidadeSaudeMockMvc.perform(get("/api/unidade-saudes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnidadeSaude() throws Exception {
        // Initialize the database
        unidadeSaudeRepository.saveAndFlush(unidadeSaude);

        int databaseSizeBeforeUpdate = unidadeSaudeRepository.findAll().size();

        // Update the unidadeSaude
        UnidadeSaude updatedUnidadeSaude = unidadeSaudeRepository.findById(unidadeSaude.getId()).get();
        // Disconnect from session so that the updates on updatedUnidadeSaude are not directly saved in db
        em.detach(updatedUnidadeSaude);
        updatedUnidadeSaude
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .horario(UPDATED_HORARIO)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .uf(UPDATED_UF)
            .cep(UPDATED_CEP)
            .referencia(UPDATED_REFERENCIA);
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(updatedUnidadeSaude);

        restUnidadeSaudeMockMvc.perform(put("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isOk());

        // Validate the UnidadeSaude in the database
        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeUpdate);
        UnidadeSaude testUnidadeSaude = unidadeSaudeList.get(unidadeSaudeList.size() - 1);
        assertThat(testUnidadeSaude.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUnidadeSaude.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testUnidadeSaude.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testUnidadeSaude.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testUnidadeSaude.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUnidadeSaude.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testUnidadeSaude.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testUnidadeSaude.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testUnidadeSaude.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testUnidadeSaude.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testUnidadeSaude.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    public void updateNonExistingUnidadeSaude() throws Exception {
        int databaseSizeBeforeUpdate = unidadeSaudeRepository.findAll().size();

        // Create the UnidadeSaude
        UnidadeSaudeDTO unidadeSaudeDTO = unidadeSaudeMapper.toDto(unidadeSaude);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnidadeSaudeMockMvc.perform(put("/api/unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UnidadeSaude in the database
        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUnidadeSaude() throws Exception {
        // Initialize the database
        unidadeSaudeRepository.saveAndFlush(unidadeSaude);

        int databaseSizeBeforeDelete = unidadeSaudeRepository.findAll().size();

        // Delete the unidadeSaude
        restUnidadeSaudeMockMvc.perform(delete("/api/unidade-saudes/{id}", unidadeSaude.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnidadeSaude> unidadeSaudeList = unidadeSaudeRepository.findAll();
        assertThat(unidadeSaudeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnidadeSaude.class);
        UnidadeSaude unidadeSaude1 = new UnidadeSaude();
        unidadeSaude1.setId(1L);
        UnidadeSaude unidadeSaude2 = new UnidadeSaude();
        unidadeSaude2.setId(unidadeSaude1.getId());
        assertThat(unidadeSaude1).isEqualTo(unidadeSaude2);
        unidadeSaude2.setId(2L);
        assertThat(unidadeSaude1).isNotEqualTo(unidadeSaude2);
        unidadeSaude1.setId(null);
        assertThat(unidadeSaude1).isNotEqualTo(unidadeSaude2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnidadeSaudeDTO.class);
        UnidadeSaudeDTO unidadeSaudeDTO1 = new UnidadeSaudeDTO();
        unidadeSaudeDTO1.setId(1L);
        UnidadeSaudeDTO unidadeSaudeDTO2 = new UnidadeSaudeDTO();
        assertThat(unidadeSaudeDTO1).isNotEqualTo(unidadeSaudeDTO2);
        unidadeSaudeDTO2.setId(unidadeSaudeDTO1.getId());
        assertThat(unidadeSaudeDTO1).isEqualTo(unidadeSaudeDTO2);
        unidadeSaudeDTO2.setId(2L);
        assertThat(unidadeSaudeDTO1).isNotEqualTo(unidadeSaudeDTO2);
        unidadeSaudeDTO1.setId(null);
        assertThat(unidadeSaudeDTO1).isNotEqualTo(unidadeSaudeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(unidadeSaudeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(unidadeSaudeMapper.fromId(null)).isNull();
    }
}

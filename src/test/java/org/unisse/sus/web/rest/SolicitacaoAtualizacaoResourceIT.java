package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.SolicitacaoAtualizacao;
import org.unisse.sus.domain.UnidadeSaude;
import org.unisse.sus.repository.SolicitacaoAtualizacaoRepository;
import org.unisse.sus.service.SolicitacaoAtualizacaoService;
import org.unisse.sus.service.dto.SolicitacaoAtualizacaoDTO;
import org.unisse.sus.service.mapper.SolicitacaoAtualizacaoMapper;
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
 * Integration tests for the {@link SolicitacaoAtualizacaoResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class SolicitacaoAtualizacaoResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SolicitacaoAtualizacaoRepository solicitacaoAtualizacaoRepository;

    @Autowired
    private SolicitacaoAtualizacaoMapper solicitacaoAtualizacaoMapper;

    @Autowired
    private SolicitacaoAtualizacaoService solicitacaoAtualizacaoService;

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

    private MockMvc restSolicitacaoAtualizacaoMockMvc;

    private SolicitacaoAtualizacao solicitacaoAtualizacao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SolicitacaoAtualizacaoResource solicitacaoAtualizacaoResource = new SolicitacaoAtualizacaoResource(solicitacaoAtualizacaoService);
        this.restSolicitacaoAtualizacaoMockMvc = MockMvcBuilders.standaloneSetup(solicitacaoAtualizacaoResource)
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
    public static SolicitacaoAtualizacao createEntity(EntityManager em) {
        SolicitacaoAtualizacao solicitacaoAtualizacao = new SolicitacaoAtualizacao()
            .criacao(DEFAULT_CRIACAO);
        // Add required entity
        UnidadeSaude unidadeSaude;
        if (TestUtil.findAll(em, UnidadeSaude.class).isEmpty()) {
            unidadeSaude = UnidadeSaudeResourceIT.createEntity(em);
            em.persist(unidadeSaude);
            em.flush();
        } else {
            unidadeSaude = TestUtil.findAll(em, UnidadeSaude.class).get(0);
        }
        solicitacaoAtualizacao.setUnidade(unidadeSaude);
        return solicitacaoAtualizacao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolicitacaoAtualizacao createUpdatedEntity(EntityManager em) {
        SolicitacaoAtualizacao solicitacaoAtualizacao = new SolicitacaoAtualizacao()
            .criacao(UPDATED_CRIACAO);
        // Add required entity
        UnidadeSaude unidadeSaude;
        if (TestUtil.findAll(em, UnidadeSaude.class).isEmpty()) {
            unidadeSaude = UnidadeSaudeResourceIT.createUpdatedEntity(em);
            em.persist(unidadeSaude);
            em.flush();
        } else {
            unidadeSaude = TestUtil.findAll(em, UnidadeSaude.class).get(0);
        }
        solicitacaoAtualizacao.setUnidade(unidadeSaude);
        return solicitacaoAtualizacao;
    }

    @BeforeEach
    public void initTest() {
        solicitacaoAtualizacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createSolicitacaoAtualizacao() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoAtualizacaoRepository.findAll().size();

        // Create the SolicitacaoAtualizacao
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO = solicitacaoAtualizacaoMapper.toDto(solicitacaoAtualizacao);
        restSolicitacaoAtualizacaoMockMvc.perform(post("/api/solicitacao-atualizacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoAtualizacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the SolicitacaoAtualizacao in the database
        List<SolicitacaoAtualizacao> solicitacaoAtualizacaoList = solicitacaoAtualizacaoRepository.findAll();
        assertThat(solicitacaoAtualizacaoList).hasSize(databaseSizeBeforeCreate + 1);
        SolicitacaoAtualizacao testSolicitacaoAtualizacao = solicitacaoAtualizacaoList.get(solicitacaoAtualizacaoList.size() - 1);
        assertThat(testSolicitacaoAtualizacao.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
    }

    @Test
    @Transactional
    public void createSolicitacaoAtualizacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoAtualizacaoRepository.findAll().size();

        // Create the SolicitacaoAtualizacao with an existing ID
        solicitacaoAtualizacao.setId(1L);
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO = solicitacaoAtualizacaoMapper.toDto(solicitacaoAtualizacao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitacaoAtualizacaoMockMvc.perform(post("/api/solicitacao-atualizacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoAtualizacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoAtualizacao in the database
        List<SolicitacaoAtualizacao> solicitacaoAtualizacaoList = solicitacaoAtualizacaoRepository.findAll();
        assertThat(solicitacaoAtualizacaoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoAtualizacaoRepository.findAll().size();
        // set the field null
        solicitacaoAtualizacao.setCriacao(null);

        // Create the SolicitacaoAtualizacao, which fails.
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO = solicitacaoAtualizacaoMapper.toDto(solicitacaoAtualizacao);

        restSolicitacaoAtualizacaoMockMvc.perform(post("/api/solicitacao-atualizacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoAtualizacaoDTO)))
            .andExpect(status().isBadRequest());

        List<SolicitacaoAtualizacao> solicitacaoAtualizacaoList = solicitacaoAtualizacaoRepository.findAll();
        assertThat(solicitacaoAtualizacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSolicitacaoAtualizacaos() throws Exception {
        // Initialize the database
        solicitacaoAtualizacaoRepository.saveAndFlush(solicitacaoAtualizacao);

        // Get all the solicitacaoAtualizacaoList
        restSolicitacaoAtualizacaoMockMvc.perform(get("/api/solicitacao-atualizacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacaoAtualizacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())));
    }
    
    @Test
    @Transactional
    public void getSolicitacaoAtualizacao() throws Exception {
        // Initialize the database
        solicitacaoAtualizacaoRepository.saveAndFlush(solicitacaoAtualizacao);

        // Get the solicitacaoAtualizacao
        restSolicitacaoAtualizacaoMockMvc.perform(get("/api/solicitacao-atualizacaos/{id}", solicitacaoAtualizacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(solicitacaoAtualizacao.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSolicitacaoAtualizacao() throws Exception {
        // Get the solicitacaoAtualizacao
        restSolicitacaoAtualizacaoMockMvc.perform(get("/api/solicitacao-atualizacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolicitacaoAtualizacao() throws Exception {
        // Initialize the database
        solicitacaoAtualizacaoRepository.saveAndFlush(solicitacaoAtualizacao);

        int databaseSizeBeforeUpdate = solicitacaoAtualizacaoRepository.findAll().size();

        // Update the solicitacaoAtualizacao
        SolicitacaoAtualizacao updatedSolicitacaoAtualizacao = solicitacaoAtualizacaoRepository.findById(solicitacaoAtualizacao.getId()).get();
        // Disconnect from session so that the updates on updatedSolicitacaoAtualizacao are not directly saved in db
        em.detach(updatedSolicitacaoAtualizacao);
        updatedSolicitacaoAtualizacao
            .criacao(UPDATED_CRIACAO);
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO = solicitacaoAtualizacaoMapper.toDto(updatedSolicitacaoAtualizacao);

        restSolicitacaoAtualizacaoMockMvc.perform(put("/api/solicitacao-atualizacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoAtualizacaoDTO)))
            .andExpect(status().isOk());

        // Validate the SolicitacaoAtualizacao in the database
        List<SolicitacaoAtualizacao> solicitacaoAtualizacaoList = solicitacaoAtualizacaoRepository.findAll();
        assertThat(solicitacaoAtualizacaoList).hasSize(databaseSizeBeforeUpdate);
        SolicitacaoAtualizacao testSolicitacaoAtualizacao = solicitacaoAtualizacaoList.get(solicitacaoAtualizacaoList.size() - 1);
        assertThat(testSolicitacaoAtualizacao.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingSolicitacaoAtualizacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoAtualizacaoRepository.findAll().size();

        // Create the SolicitacaoAtualizacao
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO = solicitacaoAtualizacaoMapper.toDto(solicitacaoAtualizacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoAtualizacaoMockMvc.perform(put("/api/solicitacao-atualizacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoAtualizacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoAtualizacao in the database
        List<SolicitacaoAtualizacao> solicitacaoAtualizacaoList = solicitacaoAtualizacaoRepository.findAll();
        assertThat(solicitacaoAtualizacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSolicitacaoAtualizacao() throws Exception {
        // Initialize the database
        solicitacaoAtualizacaoRepository.saveAndFlush(solicitacaoAtualizacao);

        int databaseSizeBeforeDelete = solicitacaoAtualizacaoRepository.findAll().size();

        // Delete the solicitacaoAtualizacao
        restSolicitacaoAtualizacaoMockMvc.perform(delete("/api/solicitacao-atualizacaos/{id}", solicitacaoAtualizacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolicitacaoAtualizacao> solicitacaoAtualizacaoList = solicitacaoAtualizacaoRepository.findAll();
        assertThat(solicitacaoAtualizacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoAtualizacao.class);
        SolicitacaoAtualizacao solicitacaoAtualizacao1 = new SolicitacaoAtualizacao();
        solicitacaoAtualizacao1.setId(1L);
        SolicitacaoAtualizacao solicitacaoAtualizacao2 = new SolicitacaoAtualizacao();
        solicitacaoAtualizacao2.setId(solicitacaoAtualizacao1.getId());
        assertThat(solicitacaoAtualizacao1).isEqualTo(solicitacaoAtualizacao2);
        solicitacaoAtualizacao2.setId(2L);
        assertThat(solicitacaoAtualizacao1).isNotEqualTo(solicitacaoAtualizacao2);
        solicitacaoAtualizacao1.setId(null);
        assertThat(solicitacaoAtualizacao1).isNotEqualTo(solicitacaoAtualizacao2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoAtualizacaoDTO.class);
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO1 = new SolicitacaoAtualizacaoDTO();
        solicitacaoAtualizacaoDTO1.setId(1L);
        SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO2 = new SolicitacaoAtualizacaoDTO();
        assertThat(solicitacaoAtualizacaoDTO1).isNotEqualTo(solicitacaoAtualizacaoDTO2);
        solicitacaoAtualizacaoDTO2.setId(solicitacaoAtualizacaoDTO1.getId());
        assertThat(solicitacaoAtualizacaoDTO1).isEqualTo(solicitacaoAtualizacaoDTO2);
        solicitacaoAtualizacaoDTO2.setId(2L);
        assertThat(solicitacaoAtualizacaoDTO1).isNotEqualTo(solicitacaoAtualizacaoDTO2);
        solicitacaoAtualizacaoDTO1.setId(null);
        assertThat(solicitacaoAtualizacaoDTO1).isNotEqualTo(solicitacaoAtualizacaoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(solicitacaoAtualizacaoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(solicitacaoAtualizacaoMapper.fromId(null)).isNull();
    }
}

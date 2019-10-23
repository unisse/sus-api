package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.SolicitacaoCriacao;
import org.unisse.sus.repository.SolicitacaoCriacaoRepository;
import org.unisse.sus.service.SolicitacaoCriacaoService;
import org.unisse.sus.service.dto.SolicitacaoCriacaoDTO;
import org.unisse.sus.service.mapper.SolicitacaoCriacaoMapper;
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
 * Integration tests for the {@link SolicitacaoCriacaoResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class SolicitacaoCriacaoResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SolicitacaoCriacaoRepository solicitacaoCriacaoRepository;

    @Autowired
    private SolicitacaoCriacaoMapper solicitacaoCriacaoMapper;

    @Autowired
    private SolicitacaoCriacaoService solicitacaoCriacaoService;

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

    private MockMvc restSolicitacaoCriacaoMockMvc;

    private SolicitacaoCriacao solicitacaoCriacao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SolicitacaoCriacaoResource solicitacaoCriacaoResource = new SolicitacaoCriacaoResource(solicitacaoCriacaoService);
        this.restSolicitacaoCriacaoMockMvc = MockMvcBuilders.standaloneSetup(solicitacaoCriacaoResource)
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
    public static SolicitacaoCriacao createEntity(EntityManager em) {
        SolicitacaoCriacao solicitacaoCriacao = new SolicitacaoCriacao()
            .criacao(DEFAULT_CRIACAO);
        return solicitacaoCriacao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolicitacaoCriacao createUpdatedEntity(EntityManager em) {
        SolicitacaoCriacao solicitacaoCriacao = new SolicitacaoCriacao()
            .criacao(UPDATED_CRIACAO);
        return solicitacaoCriacao;
    }

    @BeforeEach
    public void initTest() {
        solicitacaoCriacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createSolicitacaoCriacao() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoCriacaoRepository.findAll().size();

        // Create the SolicitacaoCriacao
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO = solicitacaoCriacaoMapper.toDto(solicitacaoCriacao);
        restSolicitacaoCriacaoMockMvc.perform(post("/api/solicitacao-criacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoCriacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the SolicitacaoCriacao in the database
        List<SolicitacaoCriacao> solicitacaoCriacaoList = solicitacaoCriacaoRepository.findAll();
        assertThat(solicitacaoCriacaoList).hasSize(databaseSizeBeforeCreate + 1);
        SolicitacaoCriacao testSolicitacaoCriacao = solicitacaoCriacaoList.get(solicitacaoCriacaoList.size() - 1);
        assertThat(testSolicitacaoCriacao.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
    }

    @Test
    @Transactional
    public void createSolicitacaoCriacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoCriacaoRepository.findAll().size();

        // Create the SolicitacaoCriacao with an existing ID
        solicitacaoCriacao.setId(1L);
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO = solicitacaoCriacaoMapper.toDto(solicitacaoCriacao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitacaoCriacaoMockMvc.perform(post("/api/solicitacao-criacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoCriacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoCriacao in the database
        List<SolicitacaoCriacao> solicitacaoCriacaoList = solicitacaoCriacaoRepository.findAll();
        assertThat(solicitacaoCriacaoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoCriacaoRepository.findAll().size();
        // set the field null
        solicitacaoCriacao.setCriacao(null);

        // Create the SolicitacaoCriacao, which fails.
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO = solicitacaoCriacaoMapper.toDto(solicitacaoCriacao);

        restSolicitacaoCriacaoMockMvc.perform(post("/api/solicitacao-criacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoCriacaoDTO)))
            .andExpect(status().isBadRequest());

        List<SolicitacaoCriacao> solicitacaoCriacaoList = solicitacaoCriacaoRepository.findAll();
        assertThat(solicitacaoCriacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSolicitacaoCriacaos() throws Exception {
        // Initialize the database
        solicitacaoCriacaoRepository.saveAndFlush(solicitacaoCriacao);

        // Get all the solicitacaoCriacaoList
        restSolicitacaoCriacaoMockMvc.perform(get("/api/solicitacao-criacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacaoCriacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())));
    }
    
    @Test
    @Transactional
    public void getSolicitacaoCriacao() throws Exception {
        // Initialize the database
        solicitacaoCriacaoRepository.saveAndFlush(solicitacaoCriacao);

        // Get the solicitacaoCriacao
        restSolicitacaoCriacaoMockMvc.perform(get("/api/solicitacao-criacaos/{id}", solicitacaoCriacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(solicitacaoCriacao.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSolicitacaoCriacao() throws Exception {
        // Get the solicitacaoCriacao
        restSolicitacaoCriacaoMockMvc.perform(get("/api/solicitacao-criacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolicitacaoCriacao() throws Exception {
        // Initialize the database
        solicitacaoCriacaoRepository.saveAndFlush(solicitacaoCriacao);

        int databaseSizeBeforeUpdate = solicitacaoCriacaoRepository.findAll().size();

        // Update the solicitacaoCriacao
        SolicitacaoCriacao updatedSolicitacaoCriacao = solicitacaoCriacaoRepository.findById(solicitacaoCriacao.getId()).get();
        // Disconnect from session so that the updates on updatedSolicitacaoCriacao are not directly saved in db
        em.detach(updatedSolicitacaoCriacao);
        updatedSolicitacaoCriacao
            .criacao(UPDATED_CRIACAO);
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO = solicitacaoCriacaoMapper.toDto(updatedSolicitacaoCriacao);

        restSolicitacaoCriacaoMockMvc.perform(put("/api/solicitacao-criacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoCriacaoDTO)))
            .andExpect(status().isOk());

        // Validate the SolicitacaoCriacao in the database
        List<SolicitacaoCriacao> solicitacaoCriacaoList = solicitacaoCriacaoRepository.findAll();
        assertThat(solicitacaoCriacaoList).hasSize(databaseSizeBeforeUpdate);
        SolicitacaoCriacao testSolicitacaoCriacao = solicitacaoCriacaoList.get(solicitacaoCriacaoList.size() - 1);
        assertThat(testSolicitacaoCriacao.getCriacao()).isEqualTo(UPDATED_CRIACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingSolicitacaoCriacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoCriacaoRepository.findAll().size();

        // Create the SolicitacaoCriacao
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO = solicitacaoCriacaoMapper.toDto(solicitacaoCriacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoCriacaoMockMvc.perform(put("/api/solicitacao-criacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitacaoCriacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SolicitacaoCriacao in the database
        List<SolicitacaoCriacao> solicitacaoCriacaoList = solicitacaoCriacaoRepository.findAll();
        assertThat(solicitacaoCriacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSolicitacaoCriacao() throws Exception {
        // Initialize the database
        solicitacaoCriacaoRepository.saveAndFlush(solicitacaoCriacao);

        int databaseSizeBeforeDelete = solicitacaoCriacaoRepository.findAll().size();

        // Delete the solicitacaoCriacao
        restSolicitacaoCriacaoMockMvc.perform(delete("/api/solicitacao-criacaos/{id}", solicitacaoCriacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolicitacaoCriacao> solicitacaoCriacaoList = solicitacaoCriacaoRepository.findAll();
        assertThat(solicitacaoCriacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoCriacao.class);
        SolicitacaoCriacao solicitacaoCriacao1 = new SolicitacaoCriacao();
        solicitacaoCriacao1.setId(1L);
        SolicitacaoCriacao solicitacaoCriacao2 = new SolicitacaoCriacao();
        solicitacaoCriacao2.setId(solicitacaoCriacao1.getId());
        assertThat(solicitacaoCriacao1).isEqualTo(solicitacaoCriacao2);
        solicitacaoCriacao2.setId(2L);
        assertThat(solicitacaoCriacao1).isNotEqualTo(solicitacaoCriacao2);
        solicitacaoCriacao1.setId(null);
        assertThat(solicitacaoCriacao1).isNotEqualTo(solicitacaoCriacao2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitacaoCriacaoDTO.class);
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO1 = new SolicitacaoCriacaoDTO();
        solicitacaoCriacaoDTO1.setId(1L);
        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO2 = new SolicitacaoCriacaoDTO();
        assertThat(solicitacaoCriacaoDTO1).isNotEqualTo(solicitacaoCriacaoDTO2);
        solicitacaoCriacaoDTO2.setId(solicitacaoCriacaoDTO1.getId());
        assertThat(solicitacaoCriacaoDTO1).isEqualTo(solicitacaoCriacaoDTO2);
        solicitacaoCriacaoDTO2.setId(2L);
        assertThat(solicitacaoCriacaoDTO1).isNotEqualTo(solicitacaoCriacaoDTO2);
        solicitacaoCriacaoDTO1.setId(null);
        assertThat(solicitacaoCriacaoDTO1).isNotEqualTo(solicitacaoCriacaoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(solicitacaoCriacaoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(solicitacaoCriacaoMapper.fromId(null)).isNull();
    }
}

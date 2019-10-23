package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.Alteracao;
import org.unisse.sus.domain.SolicitacaoAtualizacao;
import org.unisse.sus.repository.AlteracaoRepository;
import org.unisse.sus.service.AlteracaoService;
import org.unisse.sus.service.dto.AlteracaoDTO;
import org.unisse.sus.service.mapper.AlteracaoMapper;
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
import java.util.List;

import static org.unisse.sus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AlteracaoResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class AlteracaoResourceIT {

    private static final String DEFAULT_NOME_CAMPO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_CAMPO = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR_ANTERIOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR_ANTERIOR = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR_NOVO = "AAAAAAAAAA";
    private static final String UPDATED_VALOR_NOVO = "BBBBBBBBBB";

    @Autowired
    private AlteracaoRepository alteracaoRepository;

    @Autowired
    private AlteracaoMapper alteracaoMapper;

    @Autowired
    private AlteracaoService alteracaoService;

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

    private MockMvc restAlteracaoMockMvc;

    private Alteracao alteracao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlteracaoResource alteracaoResource = new AlteracaoResource(alteracaoService);
        this.restAlteracaoMockMvc = MockMvcBuilders.standaloneSetup(alteracaoResource)
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
    public static Alteracao createEntity(EntityManager em) {
        Alteracao alteracao = new Alteracao()
            .nomeCampo(DEFAULT_NOME_CAMPO)
            .valorAnterior(DEFAULT_VALOR_ANTERIOR)
            .valorNovo(DEFAULT_VALOR_NOVO);
        // Add required entity
        SolicitacaoAtualizacao solicitacaoAtualizacao;
        if (TestUtil.findAll(em, SolicitacaoAtualizacao.class).isEmpty()) {
            solicitacaoAtualizacao = SolicitacaoAtualizacaoResourceIT.createEntity(em);
            em.persist(solicitacaoAtualizacao);
            em.flush();
        } else {
            solicitacaoAtualizacao = TestUtil.findAll(em, SolicitacaoAtualizacao.class).get(0);
        }
        alteracao.setSolicitacao(solicitacaoAtualizacao);
        return alteracao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alteracao createUpdatedEntity(EntityManager em) {
        Alteracao alteracao = new Alteracao()
            .nomeCampo(UPDATED_NOME_CAMPO)
            .valorAnterior(UPDATED_VALOR_ANTERIOR)
            .valorNovo(UPDATED_VALOR_NOVO);
        // Add required entity
        SolicitacaoAtualizacao solicitacaoAtualizacao;
        if (TestUtil.findAll(em, SolicitacaoAtualizacao.class).isEmpty()) {
            solicitacaoAtualizacao = SolicitacaoAtualizacaoResourceIT.createUpdatedEntity(em);
            em.persist(solicitacaoAtualizacao);
            em.flush();
        } else {
            solicitacaoAtualizacao = TestUtil.findAll(em, SolicitacaoAtualizacao.class).get(0);
        }
        alteracao.setSolicitacao(solicitacaoAtualizacao);
        return alteracao;
    }

    @BeforeEach
    public void initTest() {
        alteracao = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlteracao() throws Exception {
        int databaseSizeBeforeCreate = alteracaoRepository.findAll().size();

        // Create the Alteracao
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(alteracao);
        restAlteracaoMockMvc.perform(post("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Alteracao in the database
        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeCreate + 1);
        Alteracao testAlteracao = alteracaoList.get(alteracaoList.size() - 1);
        assertThat(testAlteracao.getNomeCampo()).isEqualTo(DEFAULT_NOME_CAMPO);
        assertThat(testAlteracao.getValorAnterior()).isEqualTo(DEFAULT_VALOR_ANTERIOR);
        assertThat(testAlteracao.getValorNovo()).isEqualTo(DEFAULT_VALOR_NOVO);
    }

    @Test
    @Transactional
    public void createAlteracaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alteracaoRepository.findAll().size();

        // Create the Alteracao with an existing ID
        alteracao.setId(1L);
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(alteracao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlteracaoMockMvc.perform(post("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alteracao in the database
        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeCampoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alteracaoRepository.findAll().size();
        // set the field null
        alteracao.setNomeCampo(null);

        // Create the Alteracao, which fails.
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(alteracao);

        restAlteracaoMockMvc.perform(post("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isBadRequest());

        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorAnteriorIsRequired() throws Exception {
        int databaseSizeBeforeTest = alteracaoRepository.findAll().size();
        // set the field null
        alteracao.setValorAnterior(null);

        // Create the Alteracao, which fails.
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(alteracao);

        restAlteracaoMockMvc.perform(post("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isBadRequest());

        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorNovoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alteracaoRepository.findAll().size();
        // set the field null
        alteracao.setValorNovo(null);

        // Create the Alteracao, which fails.
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(alteracao);

        restAlteracaoMockMvc.perform(post("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isBadRequest());

        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlteracaos() throws Exception {
        // Initialize the database
        alteracaoRepository.saveAndFlush(alteracao);

        // Get all the alteracaoList
        restAlteracaoMockMvc.perform(get("/api/alteracaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alteracao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCampo").value(hasItem(DEFAULT_NOME_CAMPO)))
            .andExpect(jsonPath("$.[*].valorAnterior").value(hasItem(DEFAULT_VALOR_ANTERIOR)))
            .andExpect(jsonPath("$.[*].valorNovo").value(hasItem(DEFAULT_VALOR_NOVO)));
    }
    
    @Test
    @Transactional
    public void getAlteracao() throws Exception {
        // Initialize the database
        alteracaoRepository.saveAndFlush(alteracao);

        // Get the alteracao
        restAlteracaoMockMvc.perform(get("/api/alteracaos/{id}", alteracao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alteracao.getId().intValue()))
            .andExpect(jsonPath("$.nomeCampo").value(DEFAULT_NOME_CAMPO))
            .andExpect(jsonPath("$.valorAnterior").value(DEFAULT_VALOR_ANTERIOR))
            .andExpect(jsonPath("$.valorNovo").value(DEFAULT_VALOR_NOVO));
    }

    @Test
    @Transactional
    public void getNonExistingAlteracao() throws Exception {
        // Get the alteracao
        restAlteracaoMockMvc.perform(get("/api/alteracaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlteracao() throws Exception {
        // Initialize the database
        alteracaoRepository.saveAndFlush(alteracao);

        int databaseSizeBeforeUpdate = alteracaoRepository.findAll().size();

        // Update the alteracao
        Alteracao updatedAlteracao = alteracaoRepository.findById(alteracao.getId()).get();
        // Disconnect from session so that the updates on updatedAlteracao are not directly saved in db
        em.detach(updatedAlteracao);
        updatedAlteracao
            .nomeCampo(UPDATED_NOME_CAMPO)
            .valorAnterior(UPDATED_VALOR_ANTERIOR)
            .valorNovo(UPDATED_VALOR_NOVO);
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(updatedAlteracao);

        restAlteracaoMockMvc.perform(put("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isOk());

        // Validate the Alteracao in the database
        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeUpdate);
        Alteracao testAlteracao = alteracaoList.get(alteracaoList.size() - 1);
        assertThat(testAlteracao.getNomeCampo()).isEqualTo(UPDATED_NOME_CAMPO);
        assertThat(testAlteracao.getValorAnterior()).isEqualTo(UPDATED_VALOR_ANTERIOR);
        assertThat(testAlteracao.getValorNovo()).isEqualTo(UPDATED_VALOR_NOVO);
    }

    @Test
    @Transactional
    public void updateNonExistingAlteracao() throws Exception {
        int databaseSizeBeforeUpdate = alteracaoRepository.findAll().size();

        // Create the Alteracao
        AlteracaoDTO alteracaoDTO = alteracaoMapper.toDto(alteracao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlteracaoMockMvc.perform(put("/api/alteracaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alteracaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alteracao in the database
        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlteracao() throws Exception {
        // Initialize the database
        alteracaoRepository.saveAndFlush(alteracao);

        int databaseSizeBeforeDelete = alteracaoRepository.findAll().size();

        // Delete the alteracao
        restAlteracaoMockMvc.perform(delete("/api/alteracaos/{id}", alteracao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alteracao> alteracaoList = alteracaoRepository.findAll();
        assertThat(alteracaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alteracao.class);
        Alteracao alteracao1 = new Alteracao();
        alteracao1.setId(1L);
        Alteracao alteracao2 = new Alteracao();
        alteracao2.setId(alteracao1.getId());
        assertThat(alteracao1).isEqualTo(alteracao2);
        alteracao2.setId(2L);
        assertThat(alteracao1).isNotEqualTo(alteracao2);
        alteracao1.setId(null);
        assertThat(alteracao1).isNotEqualTo(alteracao2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlteracaoDTO.class);
        AlteracaoDTO alteracaoDTO1 = new AlteracaoDTO();
        alteracaoDTO1.setId(1L);
        AlteracaoDTO alteracaoDTO2 = new AlteracaoDTO();
        assertThat(alteracaoDTO1).isNotEqualTo(alteracaoDTO2);
        alteracaoDTO2.setId(alteracaoDTO1.getId());
        assertThat(alteracaoDTO1).isEqualTo(alteracaoDTO2);
        alteracaoDTO2.setId(2L);
        assertThat(alteracaoDTO1).isNotEqualTo(alteracaoDTO2);
        alteracaoDTO1.setId(null);
        assertThat(alteracaoDTO1).isNotEqualTo(alteracaoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(alteracaoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(alteracaoMapper.fromId(null)).isNull();
    }
}

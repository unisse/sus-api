package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.SituacaoUnidadeSaude;
import org.unisse.sus.repository.SituacaoUnidadeSaudeRepository;
import org.unisse.sus.service.SituacaoUnidadeSaudeService;
import org.unisse.sus.service.dto.SituacaoUnidadeSaudeDTO;
import org.unisse.sus.service.mapper.SituacaoUnidadeSaudeMapper;
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
 * Integration tests for the {@link SituacaoUnidadeSaudeResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class SituacaoUnidadeSaudeResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private SituacaoUnidadeSaudeRepository situacaoUnidadeSaudeRepository;

    @Autowired
    private SituacaoUnidadeSaudeMapper situacaoUnidadeSaudeMapper;

    @Autowired
    private SituacaoUnidadeSaudeService situacaoUnidadeSaudeService;

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

    private MockMvc restSituacaoUnidadeSaudeMockMvc;

    private SituacaoUnidadeSaude situacaoUnidadeSaude;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SituacaoUnidadeSaudeResource situacaoUnidadeSaudeResource = new SituacaoUnidadeSaudeResource(situacaoUnidadeSaudeService);
        this.restSituacaoUnidadeSaudeMockMvc = MockMvcBuilders.standaloneSetup(situacaoUnidadeSaudeResource)
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
    public static SituacaoUnidadeSaude createEntity(EntityManager em) {
        SituacaoUnidadeSaude situacaoUnidadeSaude = new SituacaoUnidadeSaude()
            .criacao(DEFAULT_CRIACAO)
            .descricao(DEFAULT_DESCRICAO);
        return situacaoUnidadeSaude;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SituacaoUnidadeSaude createUpdatedEntity(EntityManager em) {
        SituacaoUnidadeSaude situacaoUnidadeSaude = new SituacaoUnidadeSaude()
            .criacao(UPDATED_CRIACAO)
            .descricao(UPDATED_DESCRICAO);
        return situacaoUnidadeSaude;
    }

    @BeforeEach
    public void initTest() {
        situacaoUnidadeSaude = createEntity(em);
    }

    @Test
    @Transactional
    public void createSituacaoUnidadeSaude() throws Exception {
        int databaseSizeBeforeCreate = situacaoUnidadeSaudeRepository.findAll().size();

        // Create the SituacaoUnidadeSaude
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeMapper.toDto(situacaoUnidadeSaude);
        restSituacaoUnidadeSaudeMockMvc.perform(post("/api/situacao-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacaoUnidadeSaudeDTO)))
            .andExpect(status().isCreated());

        // Validate the SituacaoUnidadeSaude in the database
        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeCreate + 1);
        SituacaoUnidadeSaude testSituacaoUnidadeSaude = situacaoUnidadeSaudeList.get(situacaoUnidadeSaudeList.size() - 1);
        assertThat(testSituacaoUnidadeSaude.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
        assertThat(testSituacaoUnidadeSaude.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createSituacaoUnidadeSaudeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = situacaoUnidadeSaudeRepository.findAll().size();

        // Create the SituacaoUnidadeSaude with an existing ID
        situacaoUnidadeSaude.setId(1L);
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeMapper.toDto(situacaoUnidadeSaude);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSituacaoUnidadeSaudeMockMvc.perform(post("/api/situacao-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacaoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SituacaoUnidadeSaude in the database
        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = situacaoUnidadeSaudeRepository.findAll().size();
        // set the field null
        situacaoUnidadeSaude.setCriacao(null);

        // Create the SituacaoUnidadeSaude, which fails.
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeMapper.toDto(situacaoUnidadeSaude);

        restSituacaoUnidadeSaudeMockMvc.perform(post("/api/situacao-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacaoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = situacaoUnidadeSaudeRepository.findAll().size();
        // set the field null
        situacaoUnidadeSaude.setDescricao(null);

        // Create the SituacaoUnidadeSaude, which fails.
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeMapper.toDto(situacaoUnidadeSaude);

        restSituacaoUnidadeSaudeMockMvc.perform(post("/api/situacao-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacaoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSituacaoUnidadeSaudes() throws Exception {
        // Initialize the database
        situacaoUnidadeSaudeRepository.saveAndFlush(situacaoUnidadeSaude);

        // Get all the situacaoUnidadeSaudeList
        restSituacaoUnidadeSaudeMockMvc.perform(get("/api/situacao-unidade-saudes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situacaoUnidadeSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getSituacaoUnidadeSaude() throws Exception {
        // Initialize the database
        situacaoUnidadeSaudeRepository.saveAndFlush(situacaoUnidadeSaude);

        // Get the situacaoUnidadeSaude
        restSituacaoUnidadeSaudeMockMvc.perform(get("/api/situacao-unidade-saudes/{id}", situacaoUnidadeSaude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(situacaoUnidadeSaude.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingSituacaoUnidadeSaude() throws Exception {
        // Get the situacaoUnidadeSaude
        restSituacaoUnidadeSaudeMockMvc.perform(get("/api/situacao-unidade-saudes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSituacaoUnidadeSaude() throws Exception {
        // Initialize the database
        situacaoUnidadeSaudeRepository.saveAndFlush(situacaoUnidadeSaude);

        int databaseSizeBeforeUpdate = situacaoUnidadeSaudeRepository.findAll().size();

        // Update the situacaoUnidadeSaude
        SituacaoUnidadeSaude updatedSituacaoUnidadeSaude = situacaoUnidadeSaudeRepository.findById(situacaoUnidadeSaude.getId()).get();
        // Disconnect from session so that the updates on updatedSituacaoUnidadeSaude are not directly saved in db
        em.detach(updatedSituacaoUnidadeSaude);
        updatedSituacaoUnidadeSaude
            .criacao(UPDATED_CRIACAO)
            .descricao(UPDATED_DESCRICAO);
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeMapper.toDto(updatedSituacaoUnidadeSaude);

        restSituacaoUnidadeSaudeMockMvc.perform(put("/api/situacao-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacaoUnidadeSaudeDTO)))
            .andExpect(status().isOk());

        // Validate the SituacaoUnidadeSaude in the database
        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeUpdate);
        SituacaoUnidadeSaude testSituacaoUnidadeSaude = situacaoUnidadeSaudeList.get(situacaoUnidadeSaudeList.size() - 1);
        assertThat(testSituacaoUnidadeSaude.getCriacao()).isEqualTo(UPDATED_CRIACAO);
        assertThat(testSituacaoUnidadeSaude.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingSituacaoUnidadeSaude() throws Exception {
        int databaseSizeBeforeUpdate = situacaoUnidadeSaudeRepository.findAll().size();

        // Create the SituacaoUnidadeSaude
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeMapper.toDto(situacaoUnidadeSaude);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSituacaoUnidadeSaudeMockMvc.perform(put("/api/situacao-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacaoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SituacaoUnidadeSaude in the database
        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSituacaoUnidadeSaude() throws Exception {
        // Initialize the database
        situacaoUnidadeSaudeRepository.saveAndFlush(situacaoUnidadeSaude);

        int databaseSizeBeforeDelete = situacaoUnidadeSaudeRepository.findAll().size();

        // Delete the situacaoUnidadeSaude
        restSituacaoUnidadeSaudeMockMvc.perform(delete("/api/situacao-unidade-saudes/{id}", situacaoUnidadeSaude.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SituacaoUnidadeSaude> situacaoUnidadeSaudeList = situacaoUnidadeSaudeRepository.findAll();
        assertThat(situacaoUnidadeSaudeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SituacaoUnidadeSaude.class);
        SituacaoUnidadeSaude situacaoUnidadeSaude1 = new SituacaoUnidadeSaude();
        situacaoUnidadeSaude1.setId(1L);
        SituacaoUnidadeSaude situacaoUnidadeSaude2 = new SituacaoUnidadeSaude();
        situacaoUnidadeSaude2.setId(situacaoUnidadeSaude1.getId());
        assertThat(situacaoUnidadeSaude1).isEqualTo(situacaoUnidadeSaude2);
        situacaoUnidadeSaude2.setId(2L);
        assertThat(situacaoUnidadeSaude1).isNotEqualTo(situacaoUnidadeSaude2);
        situacaoUnidadeSaude1.setId(null);
        assertThat(situacaoUnidadeSaude1).isNotEqualTo(situacaoUnidadeSaude2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SituacaoUnidadeSaudeDTO.class);
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO1 = new SituacaoUnidadeSaudeDTO();
        situacaoUnidadeSaudeDTO1.setId(1L);
        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO2 = new SituacaoUnidadeSaudeDTO();
        assertThat(situacaoUnidadeSaudeDTO1).isNotEqualTo(situacaoUnidadeSaudeDTO2);
        situacaoUnidadeSaudeDTO2.setId(situacaoUnidadeSaudeDTO1.getId());
        assertThat(situacaoUnidadeSaudeDTO1).isEqualTo(situacaoUnidadeSaudeDTO2);
        situacaoUnidadeSaudeDTO2.setId(2L);
        assertThat(situacaoUnidadeSaudeDTO1).isNotEqualTo(situacaoUnidadeSaudeDTO2);
        situacaoUnidadeSaudeDTO1.setId(null);
        assertThat(situacaoUnidadeSaudeDTO1).isNotEqualTo(situacaoUnidadeSaudeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(situacaoUnidadeSaudeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(situacaoUnidadeSaudeMapper.fromId(null)).isNull();
    }
}

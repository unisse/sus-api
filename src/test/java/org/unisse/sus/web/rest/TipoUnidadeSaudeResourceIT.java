package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.TipoUnidadeSaude;
import org.unisse.sus.repository.TipoUnidadeSaudeRepository;
import org.unisse.sus.service.TipoUnidadeSaudeService;
import org.unisse.sus.service.dto.TipoUnidadeSaudeDTO;
import org.unisse.sus.service.mapper.TipoUnidadeSaudeMapper;
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
 * Integration tests for the {@link TipoUnidadeSaudeResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class TipoUnidadeSaudeResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private TipoUnidadeSaudeRepository tipoUnidadeSaudeRepository;

    @Autowired
    private TipoUnidadeSaudeMapper tipoUnidadeSaudeMapper;

    @Autowired
    private TipoUnidadeSaudeService tipoUnidadeSaudeService;

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

    private MockMvc restTipoUnidadeSaudeMockMvc;

    private TipoUnidadeSaude tipoUnidadeSaude;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoUnidadeSaudeResource tipoUnidadeSaudeResource = new TipoUnidadeSaudeResource(tipoUnidadeSaudeService);
        this.restTipoUnidadeSaudeMockMvc = MockMvcBuilders.standaloneSetup(tipoUnidadeSaudeResource)
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
    public static TipoUnidadeSaude createEntity(EntityManager em) {
        TipoUnidadeSaude tipoUnidadeSaude = new TipoUnidadeSaude()
            .descricao(DEFAULT_DESCRICAO);
        return tipoUnidadeSaude;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoUnidadeSaude createUpdatedEntity(EntityManager em) {
        TipoUnidadeSaude tipoUnidadeSaude = new TipoUnidadeSaude()
            .descricao(UPDATED_DESCRICAO);
        return tipoUnidadeSaude;
    }

    @BeforeEach
    public void initTest() {
        tipoUnidadeSaude = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoUnidadeSaude() throws Exception {
        int databaseSizeBeforeCreate = tipoUnidadeSaudeRepository.findAll().size();

        // Create the TipoUnidadeSaude
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO = tipoUnidadeSaudeMapper.toDto(tipoUnidadeSaude);
        restTipoUnidadeSaudeMockMvc.perform(post("/api/tipo-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoUnidadeSaudeDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoUnidadeSaude in the database
        List<TipoUnidadeSaude> tipoUnidadeSaudeList = tipoUnidadeSaudeRepository.findAll();
        assertThat(tipoUnidadeSaudeList).hasSize(databaseSizeBeforeCreate + 1);
        TipoUnidadeSaude testTipoUnidadeSaude = tipoUnidadeSaudeList.get(tipoUnidadeSaudeList.size() - 1);
        assertThat(testTipoUnidadeSaude.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTipoUnidadeSaudeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoUnidadeSaudeRepository.findAll().size();

        // Create the TipoUnidadeSaude with an existing ID
        tipoUnidadeSaude.setId(1L);
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO = tipoUnidadeSaudeMapper.toDto(tipoUnidadeSaude);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoUnidadeSaudeMockMvc.perform(post("/api/tipo-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoUnidadeSaude in the database
        List<TipoUnidadeSaude> tipoUnidadeSaudeList = tipoUnidadeSaudeRepository.findAll();
        assertThat(tipoUnidadeSaudeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoUnidadeSaudeRepository.findAll().size();
        // set the field null
        tipoUnidadeSaude.setDescricao(null);

        // Create the TipoUnidadeSaude, which fails.
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO = tipoUnidadeSaudeMapper.toDto(tipoUnidadeSaude);

        restTipoUnidadeSaudeMockMvc.perform(post("/api/tipo-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        List<TipoUnidadeSaude> tipoUnidadeSaudeList = tipoUnidadeSaudeRepository.findAll();
        assertThat(tipoUnidadeSaudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoUnidadeSaudes() throws Exception {
        // Initialize the database
        tipoUnidadeSaudeRepository.saveAndFlush(tipoUnidadeSaude);

        // Get all the tipoUnidadeSaudeList
        restTipoUnidadeSaudeMockMvc.perform(get("/api/tipo-unidade-saudes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoUnidadeSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getTipoUnidadeSaude() throws Exception {
        // Initialize the database
        tipoUnidadeSaudeRepository.saveAndFlush(tipoUnidadeSaude);

        // Get the tipoUnidadeSaude
        restTipoUnidadeSaudeMockMvc.perform(get("/api/tipo-unidade-saudes/{id}", tipoUnidadeSaude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoUnidadeSaude.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingTipoUnidadeSaude() throws Exception {
        // Get the tipoUnidadeSaude
        restTipoUnidadeSaudeMockMvc.perform(get("/api/tipo-unidade-saudes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoUnidadeSaude() throws Exception {
        // Initialize the database
        tipoUnidadeSaudeRepository.saveAndFlush(tipoUnidadeSaude);

        int databaseSizeBeforeUpdate = tipoUnidadeSaudeRepository.findAll().size();

        // Update the tipoUnidadeSaude
        TipoUnidadeSaude updatedTipoUnidadeSaude = tipoUnidadeSaudeRepository.findById(tipoUnidadeSaude.getId()).get();
        // Disconnect from session so that the updates on updatedTipoUnidadeSaude are not directly saved in db
        em.detach(updatedTipoUnidadeSaude);
        updatedTipoUnidadeSaude
            .descricao(UPDATED_DESCRICAO);
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO = tipoUnidadeSaudeMapper.toDto(updatedTipoUnidadeSaude);

        restTipoUnidadeSaudeMockMvc.perform(put("/api/tipo-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoUnidadeSaudeDTO)))
            .andExpect(status().isOk());

        // Validate the TipoUnidadeSaude in the database
        List<TipoUnidadeSaude> tipoUnidadeSaudeList = tipoUnidadeSaudeRepository.findAll();
        assertThat(tipoUnidadeSaudeList).hasSize(databaseSizeBeforeUpdate);
        TipoUnidadeSaude testTipoUnidadeSaude = tipoUnidadeSaudeList.get(tipoUnidadeSaudeList.size() - 1);
        assertThat(testTipoUnidadeSaude.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoUnidadeSaude() throws Exception {
        int databaseSizeBeforeUpdate = tipoUnidadeSaudeRepository.findAll().size();

        // Create the TipoUnidadeSaude
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO = tipoUnidadeSaudeMapper.toDto(tipoUnidadeSaude);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoUnidadeSaudeMockMvc.perform(put("/api/tipo-unidade-saudes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoUnidadeSaudeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoUnidadeSaude in the database
        List<TipoUnidadeSaude> tipoUnidadeSaudeList = tipoUnidadeSaudeRepository.findAll();
        assertThat(tipoUnidadeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoUnidadeSaude() throws Exception {
        // Initialize the database
        tipoUnidadeSaudeRepository.saveAndFlush(tipoUnidadeSaude);

        int databaseSizeBeforeDelete = tipoUnidadeSaudeRepository.findAll().size();

        // Delete the tipoUnidadeSaude
        restTipoUnidadeSaudeMockMvc.perform(delete("/api/tipo-unidade-saudes/{id}", tipoUnidadeSaude.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoUnidadeSaude> tipoUnidadeSaudeList = tipoUnidadeSaudeRepository.findAll();
        assertThat(tipoUnidadeSaudeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoUnidadeSaude.class);
        TipoUnidadeSaude tipoUnidadeSaude1 = new TipoUnidadeSaude();
        tipoUnidadeSaude1.setId(1L);
        TipoUnidadeSaude tipoUnidadeSaude2 = new TipoUnidadeSaude();
        tipoUnidadeSaude2.setId(tipoUnidadeSaude1.getId());
        assertThat(tipoUnidadeSaude1).isEqualTo(tipoUnidadeSaude2);
        tipoUnidadeSaude2.setId(2L);
        assertThat(tipoUnidadeSaude1).isNotEqualTo(tipoUnidadeSaude2);
        tipoUnidadeSaude1.setId(null);
        assertThat(tipoUnidadeSaude1).isNotEqualTo(tipoUnidadeSaude2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoUnidadeSaudeDTO.class);
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO1 = new TipoUnidadeSaudeDTO();
        tipoUnidadeSaudeDTO1.setId(1L);
        TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO2 = new TipoUnidadeSaudeDTO();
        assertThat(tipoUnidadeSaudeDTO1).isNotEqualTo(tipoUnidadeSaudeDTO2);
        tipoUnidadeSaudeDTO2.setId(tipoUnidadeSaudeDTO1.getId());
        assertThat(tipoUnidadeSaudeDTO1).isEqualTo(tipoUnidadeSaudeDTO2);
        tipoUnidadeSaudeDTO2.setId(2L);
        assertThat(tipoUnidadeSaudeDTO1).isNotEqualTo(tipoUnidadeSaudeDTO2);
        tipoUnidadeSaudeDTO1.setId(null);
        assertThat(tipoUnidadeSaudeDTO1).isNotEqualTo(tipoUnidadeSaudeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoUnidadeSaudeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoUnidadeSaudeMapper.fromId(null)).isNull();
    }
}

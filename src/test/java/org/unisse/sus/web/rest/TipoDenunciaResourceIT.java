package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.TipoDenuncia;
import org.unisse.sus.repository.TipoDenunciaRepository;
import org.unisse.sus.service.TipoDenunciaService;
import org.unisse.sus.service.dto.TipoDenunciaDTO;
import org.unisse.sus.service.mapper.TipoDenunciaMapper;
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
 * Integration tests for the {@link TipoDenunciaResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class TipoDenunciaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private TipoDenunciaRepository tipoDenunciaRepository;

    @Autowired
    private TipoDenunciaMapper tipoDenunciaMapper;

    @Autowired
    private TipoDenunciaService tipoDenunciaService;

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

    private MockMvc restTipoDenunciaMockMvc;

    private TipoDenuncia tipoDenuncia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoDenunciaResource tipoDenunciaResource = new TipoDenunciaResource(tipoDenunciaService);
        this.restTipoDenunciaMockMvc = MockMvcBuilders.standaloneSetup(tipoDenunciaResource)
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
    public static TipoDenuncia createEntity(EntityManager em) {
        TipoDenuncia tipoDenuncia = new TipoDenuncia()
            .descricao(DEFAULT_DESCRICAO);
        return tipoDenuncia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoDenuncia createUpdatedEntity(EntityManager em) {
        TipoDenuncia tipoDenuncia = new TipoDenuncia()
            .descricao(UPDATED_DESCRICAO);
        return tipoDenuncia;
    }

    @BeforeEach
    public void initTest() {
        tipoDenuncia = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoDenuncia() throws Exception {
        int databaseSizeBeforeCreate = tipoDenunciaRepository.findAll().size();

        // Create the TipoDenuncia
        TipoDenunciaDTO tipoDenunciaDTO = tipoDenunciaMapper.toDto(tipoDenuncia);
        restTipoDenunciaMockMvc.perform(post("/api/tipo-denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDenunciaDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoDenuncia in the database
        List<TipoDenuncia> tipoDenunciaList = tipoDenunciaRepository.findAll();
        assertThat(tipoDenunciaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoDenuncia testTipoDenuncia = tipoDenunciaList.get(tipoDenunciaList.size() - 1);
        assertThat(testTipoDenuncia.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTipoDenunciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoDenunciaRepository.findAll().size();

        // Create the TipoDenuncia with an existing ID
        tipoDenuncia.setId(1L);
        TipoDenunciaDTO tipoDenunciaDTO = tipoDenunciaMapper.toDto(tipoDenuncia);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoDenunciaMockMvc.perform(post("/api/tipo-denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDenunciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDenuncia in the database
        List<TipoDenuncia> tipoDenunciaList = tipoDenunciaRepository.findAll();
        assertThat(tipoDenunciaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDenunciaRepository.findAll().size();
        // set the field null
        tipoDenuncia.setDescricao(null);

        // Create the TipoDenuncia, which fails.
        TipoDenunciaDTO tipoDenunciaDTO = tipoDenunciaMapper.toDto(tipoDenuncia);

        restTipoDenunciaMockMvc.perform(post("/api/tipo-denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDenunciaDTO)))
            .andExpect(status().isBadRequest());

        List<TipoDenuncia> tipoDenunciaList = tipoDenunciaRepository.findAll();
        assertThat(tipoDenunciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoDenuncias() throws Exception {
        // Initialize the database
        tipoDenunciaRepository.saveAndFlush(tipoDenuncia);

        // Get all the tipoDenunciaList
        restTipoDenunciaMockMvc.perform(get("/api/tipo-denuncias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDenuncia.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getTipoDenuncia() throws Exception {
        // Initialize the database
        tipoDenunciaRepository.saveAndFlush(tipoDenuncia);

        // Get the tipoDenuncia
        restTipoDenunciaMockMvc.perform(get("/api/tipo-denuncias/{id}", tipoDenuncia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoDenuncia.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingTipoDenuncia() throws Exception {
        // Get the tipoDenuncia
        restTipoDenunciaMockMvc.perform(get("/api/tipo-denuncias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoDenuncia() throws Exception {
        // Initialize the database
        tipoDenunciaRepository.saveAndFlush(tipoDenuncia);

        int databaseSizeBeforeUpdate = tipoDenunciaRepository.findAll().size();

        // Update the tipoDenuncia
        TipoDenuncia updatedTipoDenuncia = tipoDenunciaRepository.findById(tipoDenuncia.getId()).get();
        // Disconnect from session so that the updates on updatedTipoDenuncia are not directly saved in db
        em.detach(updatedTipoDenuncia);
        updatedTipoDenuncia
            .descricao(UPDATED_DESCRICAO);
        TipoDenunciaDTO tipoDenunciaDTO = tipoDenunciaMapper.toDto(updatedTipoDenuncia);

        restTipoDenunciaMockMvc.perform(put("/api/tipo-denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDenunciaDTO)))
            .andExpect(status().isOk());

        // Validate the TipoDenuncia in the database
        List<TipoDenuncia> tipoDenunciaList = tipoDenunciaRepository.findAll();
        assertThat(tipoDenunciaList).hasSize(databaseSizeBeforeUpdate);
        TipoDenuncia testTipoDenuncia = tipoDenunciaList.get(tipoDenunciaList.size() - 1);
        assertThat(testTipoDenuncia.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = tipoDenunciaRepository.findAll().size();

        // Create the TipoDenuncia
        TipoDenunciaDTO tipoDenunciaDTO = tipoDenunciaMapper.toDto(tipoDenuncia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDenunciaMockMvc.perform(put("/api/tipo-denuncias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDenunciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDenuncia in the database
        List<TipoDenuncia> tipoDenunciaList = tipoDenunciaRepository.findAll();
        assertThat(tipoDenunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoDenuncia() throws Exception {
        // Initialize the database
        tipoDenunciaRepository.saveAndFlush(tipoDenuncia);

        int databaseSizeBeforeDelete = tipoDenunciaRepository.findAll().size();

        // Delete the tipoDenuncia
        restTipoDenunciaMockMvc.perform(delete("/api/tipo-denuncias/{id}", tipoDenuncia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoDenuncia> tipoDenunciaList = tipoDenunciaRepository.findAll();
        assertThat(tipoDenunciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDenuncia.class);
        TipoDenuncia tipoDenuncia1 = new TipoDenuncia();
        tipoDenuncia1.setId(1L);
        TipoDenuncia tipoDenuncia2 = new TipoDenuncia();
        tipoDenuncia2.setId(tipoDenuncia1.getId());
        assertThat(tipoDenuncia1).isEqualTo(tipoDenuncia2);
        tipoDenuncia2.setId(2L);
        assertThat(tipoDenuncia1).isNotEqualTo(tipoDenuncia2);
        tipoDenuncia1.setId(null);
        assertThat(tipoDenuncia1).isNotEqualTo(tipoDenuncia2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDenunciaDTO.class);
        TipoDenunciaDTO tipoDenunciaDTO1 = new TipoDenunciaDTO();
        tipoDenunciaDTO1.setId(1L);
        TipoDenunciaDTO tipoDenunciaDTO2 = new TipoDenunciaDTO();
        assertThat(tipoDenunciaDTO1).isNotEqualTo(tipoDenunciaDTO2);
        tipoDenunciaDTO2.setId(tipoDenunciaDTO1.getId());
        assertThat(tipoDenunciaDTO1).isEqualTo(tipoDenunciaDTO2);
        tipoDenunciaDTO2.setId(2L);
        assertThat(tipoDenunciaDTO1).isNotEqualTo(tipoDenunciaDTO2);
        tipoDenunciaDTO1.setId(null);
        assertThat(tipoDenunciaDTO1).isNotEqualTo(tipoDenunciaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoDenunciaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoDenunciaMapper.fromId(null)).isNull();
    }
}

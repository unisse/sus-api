package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.TipoRegistro;
import org.unisse.sus.repository.TipoRegistroRepository;
import org.unisse.sus.service.TipoRegistroService;
import org.unisse.sus.service.dto.TipoRegistroDTO;
import org.unisse.sus.service.mapper.TipoRegistroMapper;
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
 * Integration tests for the {@link TipoRegistroResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class TipoRegistroResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private TipoRegistroRepository tipoRegistroRepository;

    @Autowired
    private TipoRegistroMapper tipoRegistroMapper;

    @Autowired
    private TipoRegistroService tipoRegistroService;

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

    private MockMvc restTipoRegistroMockMvc;

    private TipoRegistro tipoRegistro;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoRegistroResource tipoRegistroResource = new TipoRegistroResource(tipoRegistroService);
        this.restTipoRegistroMockMvc = MockMvcBuilders.standaloneSetup(tipoRegistroResource)
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
    public static TipoRegistro createEntity(EntityManager em) {
        TipoRegistro tipoRegistro = new TipoRegistro()
            .descricao(DEFAULT_DESCRICAO);
        return tipoRegistro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoRegistro createUpdatedEntity(EntityManager em) {
        TipoRegistro tipoRegistro = new TipoRegistro()
            .descricao(UPDATED_DESCRICAO);
        return tipoRegistro;
    }

    @BeforeEach
    public void initTest() {
        tipoRegistro = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoRegistro() throws Exception {
        int databaseSizeBeforeCreate = tipoRegistroRepository.findAll().size();

        // Create the TipoRegistro
        TipoRegistroDTO tipoRegistroDTO = tipoRegistroMapper.toDto(tipoRegistro);
        restTipoRegistroMockMvc.perform(post("/api/tipo-registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoRegistroDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoRegistro in the database
        List<TipoRegistro> tipoRegistroList = tipoRegistroRepository.findAll();
        assertThat(tipoRegistroList).hasSize(databaseSizeBeforeCreate + 1);
        TipoRegistro testTipoRegistro = tipoRegistroList.get(tipoRegistroList.size() - 1);
        assertThat(testTipoRegistro.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTipoRegistroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoRegistroRepository.findAll().size();

        // Create the TipoRegistro with an existing ID
        tipoRegistro.setId(1L);
        TipoRegistroDTO tipoRegistroDTO = tipoRegistroMapper.toDto(tipoRegistro);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoRegistroMockMvc.perform(post("/api/tipo-registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoRegistroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoRegistro in the database
        List<TipoRegistro> tipoRegistroList = tipoRegistroRepository.findAll();
        assertThat(tipoRegistroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoRegistroRepository.findAll().size();
        // set the field null
        tipoRegistro.setDescricao(null);

        // Create the TipoRegistro, which fails.
        TipoRegistroDTO tipoRegistroDTO = tipoRegistroMapper.toDto(tipoRegistro);

        restTipoRegistroMockMvc.perform(post("/api/tipo-registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoRegistroDTO)))
            .andExpect(status().isBadRequest());

        List<TipoRegistro> tipoRegistroList = tipoRegistroRepository.findAll();
        assertThat(tipoRegistroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoRegistros() throws Exception {
        // Initialize the database
        tipoRegistroRepository.saveAndFlush(tipoRegistro);

        // Get all the tipoRegistroList
        restTipoRegistroMockMvc.perform(get("/api/tipo-registros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoRegistro.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getTipoRegistro() throws Exception {
        // Initialize the database
        tipoRegistroRepository.saveAndFlush(tipoRegistro);

        // Get the tipoRegistro
        restTipoRegistroMockMvc.perform(get("/api/tipo-registros/{id}", tipoRegistro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoRegistro.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingTipoRegistro() throws Exception {
        // Get the tipoRegistro
        restTipoRegistroMockMvc.perform(get("/api/tipo-registros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoRegistro() throws Exception {
        // Initialize the database
        tipoRegistroRepository.saveAndFlush(tipoRegistro);

        int databaseSizeBeforeUpdate = tipoRegistroRepository.findAll().size();

        // Update the tipoRegistro
        TipoRegistro updatedTipoRegistro = tipoRegistroRepository.findById(tipoRegistro.getId()).get();
        // Disconnect from session so that the updates on updatedTipoRegistro are not directly saved in db
        em.detach(updatedTipoRegistro);
        updatedTipoRegistro
            .descricao(UPDATED_DESCRICAO);
        TipoRegistroDTO tipoRegistroDTO = tipoRegistroMapper.toDto(updatedTipoRegistro);

        restTipoRegistroMockMvc.perform(put("/api/tipo-registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoRegistroDTO)))
            .andExpect(status().isOk());

        // Validate the TipoRegistro in the database
        List<TipoRegistro> tipoRegistroList = tipoRegistroRepository.findAll();
        assertThat(tipoRegistroList).hasSize(databaseSizeBeforeUpdate);
        TipoRegistro testTipoRegistro = tipoRegistroList.get(tipoRegistroList.size() - 1);
        assertThat(testTipoRegistro.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoRegistro() throws Exception {
        int databaseSizeBeforeUpdate = tipoRegistroRepository.findAll().size();

        // Create the TipoRegistro
        TipoRegistroDTO tipoRegistroDTO = tipoRegistroMapper.toDto(tipoRegistro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoRegistroMockMvc.perform(put("/api/tipo-registros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoRegistroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoRegistro in the database
        List<TipoRegistro> tipoRegistroList = tipoRegistroRepository.findAll();
        assertThat(tipoRegistroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoRegistro() throws Exception {
        // Initialize the database
        tipoRegistroRepository.saveAndFlush(tipoRegistro);

        int databaseSizeBeforeDelete = tipoRegistroRepository.findAll().size();

        // Delete the tipoRegistro
        restTipoRegistroMockMvc.perform(delete("/api/tipo-registros/{id}", tipoRegistro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoRegistro> tipoRegistroList = tipoRegistroRepository.findAll();
        assertThat(tipoRegistroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoRegistro.class);
        TipoRegistro tipoRegistro1 = new TipoRegistro();
        tipoRegistro1.setId(1L);
        TipoRegistro tipoRegistro2 = new TipoRegistro();
        tipoRegistro2.setId(tipoRegistro1.getId());
        assertThat(tipoRegistro1).isEqualTo(tipoRegistro2);
        tipoRegistro2.setId(2L);
        assertThat(tipoRegistro1).isNotEqualTo(tipoRegistro2);
        tipoRegistro1.setId(null);
        assertThat(tipoRegistro1).isNotEqualTo(tipoRegistro2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoRegistroDTO.class);
        TipoRegistroDTO tipoRegistroDTO1 = new TipoRegistroDTO();
        tipoRegistroDTO1.setId(1L);
        TipoRegistroDTO tipoRegistroDTO2 = new TipoRegistroDTO();
        assertThat(tipoRegistroDTO1).isNotEqualTo(tipoRegistroDTO2);
        tipoRegistroDTO2.setId(tipoRegistroDTO1.getId());
        assertThat(tipoRegistroDTO1).isEqualTo(tipoRegistroDTO2);
        tipoRegistroDTO2.setId(2L);
        assertThat(tipoRegistroDTO1).isNotEqualTo(tipoRegistroDTO2);
        tipoRegistroDTO1.setId(null);
        assertThat(tipoRegistroDTO1).isNotEqualTo(tipoRegistroDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoRegistroMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoRegistroMapper.fromId(null)).isNull();
    }
}

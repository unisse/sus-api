package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.Comentario;
import org.unisse.sus.domain.Registro;
import org.unisse.sus.repository.ComentarioRepository;
import org.unisse.sus.service.ComentarioService;
import org.unisse.sus.service.dto.ComentarioDTO;
import org.unisse.sus.service.mapper.ComentarioMapper;
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
 * Integration tests for the {@link ComentarioResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class ComentarioResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Autowired
    private ComentarioService comentarioService;

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

    private MockMvc restComentarioMockMvc;

    private Comentario comentario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComentarioResource comentarioResource = new ComentarioResource(comentarioService);
        this.restComentarioMockMvc = MockMvcBuilders.standaloneSetup(comentarioResource)
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
    public static Comentario createEntity(EntityManager em) {
        Comentario comentario = new Comentario()
            .criacao(DEFAULT_CRIACAO)
            .comentario(DEFAULT_COMENTARIO);
        // Add required entity
        Registro registro;
        if (TestUtil.findAll(em, Registro.class).isEmpty()) {
            registro = RegistroResourceIT.createEntity(em);
            em.persist(registro);
            em.flush();
        } else {
            registro = TestUtil.findAll(em, Registro.class).get(0);
        }
        comentario.setRegistro(registro);
        return comentario;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comentario createUpdatedEntity(EntityManager em) {
        Comentario comentario = new Comentario()
            .criacao(UPDATED_CRIACAO)
            .comentario(UPDATED_COMENTARIO);
        // Add required entity
        Registro registro;
        if (TestUtil.findAll(em, Registro.class).isEmpty()) {
            registro = RegistroResourceIT.createUpdatedEntity(em);
            em.persist(registro);
            em.flush();
        } else {
            registro = TestUtil.findAll(em, Registro.class).get(0);
        }
        comentario.setRegistro(registro);
        return comentario;
    }

    @BeforeEach
    public void initTest() {
        comentario = createEntity(em);
    }

    @Test
    @Transactional
    public void createComentario() throws Exception {
        int databaseSizeBeforeCreate = comentarioRepository.findAll().size();

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);
        restComentarioMockMvc.perform(post("/api/comentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeCreate + 1);
        Comentario testComentario = comentarioList.get(comentarioList.size() - 1);
        assertThat(testComentario.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
        assertThat(testComentario.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
    }

    @Test
    @Transactional
    public void createComentarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comentarioRepository.findAll().size();

        // Create the Comentario with an existing ID
        comentario.setId(1L);
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComentarioMockMvc.perform(post("/api/comentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = comentarioRepository.findAll().size();
        // set the field null
        comentario.setCriacao(null);

        // Create the Comentario, which fails.
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        restComentarioMockMvc.perform(post("/api/comentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
            .andExpect(status().isBadRequest());

        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComentarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = comentarioRepository.findAll().size();
        // set the field null
        comentario.setComentario(null);

        // Create the Comentario, which fails.
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        restComentarioMockMvc.perform(post("/api/comentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
            .andExpect(status().isBadRequest());

        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComentarios() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get all the comentarioList
        restComentarioMockMvc.perform(get("/api/comentarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentario.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)));
    }
    
    @Test
    @Transactional
    public void getComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        // Get the comentario
        restComentarioMockMvc.perform(get("/api/comentarios/{id}", comentario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comentario.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO));
    }

    @Test
    @Transactional
    public void getNonExistingComentario() throws Exception {
        // Get the comentario
        restComentarioMockMvc.perform(get("/api/comentarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();

        // Update the comentario
        Comentario updatedComentario = comentarioRepository.findById(comentario.getId()).get();
        // Disconnect from session so that the updates on updatedComentario are not directly saved in db
        em.detach(updatedComentario);
        updatedComentario
            .criacao(UPDATED_CRIACAO)
            .comentario(UPDATED_COMENTARIO);
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(updatedComentario);

        restComentarioMockMvc.perform(put("/api/comentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
            .andExpect(status().isOk());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
        Comentario testComentario = comentarioList.get(comentarioList.size() - 1);
        assertThat(testComentario.getCriacao()).isEqualTo(UPDATED_CRIACAO);
        assertThat(testComentario.getComentario()).isEqualTo(UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    public void updateNonExistingComentario() throws Exception {
        int databaseSizeBeforeUpdate = comentarioRepository.findAll().size();

        // Create the Comentario
        ComentarioDTO comentarioDTO = comentarioMapper.toDto(comentario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentarioMockMvc.perform(put("/api/comentarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comentario in the database
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteComentario() throws Exception {
        // Initialize the database
        comentarioRepository.saveAndFlush(comentario);

        int databaseSizeBeforeDelete = comentarioRepository.findAll().size();

        // Delete the comentario
        restComentarioMockMvc.perform(delete("/api/comentarios/{id}", comentario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comentario> comentarioList = comentarioRepository.findAll();
        assertThat(comentarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comentario.class);
        Comentario comentario1 = new Comentario();
        comentario1.setId(1L);
        Comentario comentario2 = new Comentario();
        comentario2.setId(comentario1.getId());
        assertThat(comentario1).isEqualTo(comentario2);
        comentario2.setId(2L);
        assertThat(comentario1).isNotEqualTo(comentario2);
        comentario1.setId(null);
        assertThat(comentario1).isNotEqualTo(comentario2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComentarioDTO.class);
        ComentarioDTO comentarioDTO1 = new ComentarioDTO();
        comentarioDTO1.setId(1L);
        ComentarioDTO comentarioDTO2 = new ComentarioDTO();
        assertThat(comentarioDTO1).isNotEqualTo(comentarioDTO2);
        comentarioDTO2.setId(comentarioDTO1.getId());
        assertThat(comentarioDTO1).isEqualTo(comentarioDTO2);
        comentarioDTO2.setId(2L);
        assertThat(comentarioDTO1).isNotEqualTo(comentarioDTO2);
        comentarioDTO1.setId(null);
        assertThat(comentarioDTO1).isNotEqualTo(comentarioDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(comentarioMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(comentarioMapper.fromId(null)).isNull();
    }
}

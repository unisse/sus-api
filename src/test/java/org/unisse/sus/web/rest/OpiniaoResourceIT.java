package org.unisse.sus.web.rest;

import org.unisse.sus.UnisseSusApp;
import org.unisse.sus.domain.Opiniao;
import org.unisse.sus.domain.Comentario;
import org.unisse.sus.repository.OpiniaoRepository;
import org.unisse.sus.service.OpiniaoService;
import org.unisse.sus.service.dto.OpiniaoDTO;
import org.unisse.sus.service.mapper.OpiniaoMapper;
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
 * Integration tests for the {@link OpiniaoResource} REST controller.
 */
@SpringBootTest(classes = UnisseSusApp.class)
public class OpiniaoResourceIT {

    private static final Instant DEFAULT_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_POSITIVA = false;
    private static final Boolean UPDATED_POSITIVA = true;

    @Autowired
    private OpiniaoRepository opiniaoRepository;

    @Autowired
    private OpiniaoMapper opiniaoMapper;

    @Autowired
    private OpiniaoService opiniaoService;

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

    private MockMvc restOpiniaoMockMvc;

    private Opiniao opiniao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OpiniaoResource opiniaoResource = new OpiniaoResource(opiniaoService);
        this.restOpiniaoMockMvc = MockMvcBuilders.standaloneSetup(opiniaoResource)
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
    public static Opiniao createEntity(EntityManager em) {
        Opiniao opiniao = new Opiniao()
            .criacao(DEFAULT_CRIACAO)
            .positiva(DEFAULT_POSITIVA);
        // Add required entity
        Comentario comentario;
        if (TestUtil.findAll(em, Comentario.class).isEmpty()) {
            comentario = ComentarioResourceIT.createEntity(em);
            em.persist(comentario);
            em.flush();
        } else {
            comentario = TestUtil.findAll(em, Comentario.class).get(0);
        }
        opiniao.setComentario(comentario);
        return opiniao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opiniao createUpdatedEntity(EntityManager em) {
        Opiniao opiniao = new Opiniao()
            .criacao(UPDATED_CRIACAO)
            .positiva(UPDATED_POSITIVA);
        // Add required entity
        Comentario comentario;
        if (TestUtil.findAll(em, Comentario.class).isEmpty()) {
            comentario = ComentarioResourceIT.createUpdatedEntity(em);
            em.persist(comentario);
            em.flush();
        } else {
            comentario = TestUtil.findAll(em, Comentario.class).get(0);
        }
        opiniao.setComentario(comentario);
        return opiniao;
    }

    @BeforeEach
    public void initTest() {
        opiniao = createEntity(em);
    }

    @Test
    @Transactional
    public void createOpiniao() throws Exception {
        int databaseSizeBeforeCreate = opiniaoRepository.findAll().size();

        // Create the Opiniao
        OpiniaoDTO opiniaoDTO = opiniaoMapper.toDto(opiniao);
        restOpiniaoMockMvc.perform(post("/api/opiniaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opiniaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Opiniao in the database
        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeCreate + 1);
        Opiniao testOpiniao = opiniaoList.get(opiniaoList.size() - 1);
        assertThat(testOpiniao.getCriacao()).isEqualTo(DEFAULT_CRIACAO);
        assertThat(testOpiniao.isPositiva()).isEqualTo(DEFAULT_POSITIVA);
    }

    @Test
    @Transactional
    public void createOpiniaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = opiniaoRepository.findAll().size();

        // Create the Opiniao with an existing ID
        opiniao.setId(1L);
        OpiniaoDTO opiniaoDTO = opiniaoMapper.toDto(opiniao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpiniaoMockMvc.perform(post("/api/opiniaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opiniaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Opiniao in the database
        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = opiniaoRepository.findAll().size();
        // set the field null
        opiniao.setCriacao(null);

        // Create the Opiniao, which fails.
        OpiniaoDTO opiniaoDTO = opiniaoMapper.toDto(opiniao);

        restOpiniaoMockMvc.perform(post("/api/opiniaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opiniaoDTO)))
            .andExpect(status().isBadRequest());

        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPositivaIsRequired() throws Exception {
        int databaseSizeBeforeTest = opiniaoRepository.findAll().size();
        // set the field null
        opiniao.setPositiva(null);

        // Create the Opiniao, which fails.
        OpiniaoDTO opiniaoDTO = opiniaoMapper.toDto(opiniao);

        restOpiniaoMockMvc.perform(post("/api/opiniaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opiniaoDTO)))
            .andExpect(status().isBadRequest());

        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpiniaos() throws Exception {
        // Initialize the database
        opiniaoRepository.saveAndFlush(opiniao);

        // Get all the opiniaoList
        restOpiniaoMockMvc.perform(get("/api/opiniaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opiniao.getId().intValue())))
            .andExpect(jsonPath("$.[*].criacao").value(hasItem(DEFAULT_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].positiva").value(hasItem(DEFAULT_POSITIVA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOpiniao() throws Exception {
        // Initialize the database
        opiniaoRepository.saveAndFlush(opiniao);

        // Get the opiniao
        restOpiniaoMockMvc.perform(get("/api/opiniaos/{id}", opiniao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(opiniao.getId().intValue()))
            .andExpect(jsonPath("$.criacao").value(DEFAULT_CRIACAO.toString()))
            .andExpect(jsonPath("$.positiva").value(DEFAULT_POSITIVA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOpiniao() throws Exception {
        // Get the opiniao
        restOpiniaoMockMvc.perform(get("/api/opiniaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpiniao() throws Exception {
        // Initialize the database
        opiniaoRepository.saveAndFlush(opiniao);

        int databaseSizeBeforeUpdate = opiniaoRepository.findAll().size();

        // Update the opiniao
        Opiniao updatedOpiniao = opiniaoRepository.findById(opiniao.getId()).get();
        // Disconnect from session so that the updates on updatedOpiniao are not directly saved in db
        em.detach(updatedOpiniao);
        updatedOpiniao
            .criacao(UPDATED_CRIACAO)
            .positiva(UPDATED_POSITIVA);
        OpiniaoDTO opiniaoDTO = opiniaoMapper.toDto(updatedOpiniao);

        restOpiniaoMockMvc.perform(put("/api/opiniaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opiniaoDTO)))
            .andExpect(status().isOk());

        // Validate the Opiniao in the database
        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeUpdate);
        Opiniao testOpiniao = opiniaoList.get(opiniaoList.size() - 1);
        assertThat(testOpiniao.getCriacao()).isEqualTo(UPDATED_CRIACAO);
        assertThat(testOpiniao.isPositiva()).isEqualTo(UPDATED_POSITIVA);
    }

    @Test
    @Transactional
    public void updateNonExistingOpiniao() throws Exception {
        int databaseSizeBeforeUpdate = opiniaoRepository.findAll().size();

        // Create the Opiniao
        OpiniaoDTO opiniaoDTO = opiniaoMapper.toDto(opiniao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpiniaoMockMvc.perform(put("/api/opiniaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opiniaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Opiniao in the database
        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOpiniao() throws Exception {
        // Initialize the database
        opiniaoRepository.saveAndFlush(opiniao);

        int databaseSizeBeforeDelete = opiniaoRepository.findAll().size();

        // Delete the opiniao
        restOpiniaoMockMvc.perform(delete("/api/opiniaos/{id}", opiniao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opiniao> opiniaoList = opiniaoRepository.findAll();
        assertThat(opiniaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opiniao.class);
        Opiniao opiniao1 = new Opiniao();
        opiniao1.setId(1L);
        Opiniao opiniao2 = new Opiniao();
        opiniao2.setId(opiniao1.getId());
        assertThat(opiniao1).isEqualTo(opiniao2);
        opiniao2.setId(2L);
        assertThat(opiniao1).isNotEqualTo(opiniao2);
        opiniao1.setId(null);
        assertThat(opiniao1).isNotEqualTo(opiniao2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpiniaoDTO.class);
        OpiniaoDTO opiniaoDTO1 = new OpiniaoDTO();
        opiniaoDTO1.setId(1L);
        OpiniaoDTO opiniaoDTO2 = new OpiniaoDTO();
        assertThat(opiniaoDTO1).isNotEqualTo(opiniaoDTO2);
        opiniaoDTO2.setId(opiniaoDTO1.getId());
        assertThat(opiniaoDTO1).isEqualTo(opiniaoDTO2);
        opiniaoDTO2.setId(2L);
        assertThat(opiniaoDTO1).isNotEqualTo(opiniaoDTO2);
        opiniaoDTO1.setId(null);
        assertThat(opiniaoDTO1).isNotEqualTo(opiniaoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(opiniaoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(opiniaoMapper.fromId(null)).isNull();
    }
}

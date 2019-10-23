package org.unisse.sus.web.rest;

import org.unisse.sus.service.SituacaoUnidadeSaudeService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.SituacaoUnidadeSaudeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.unisse.sus.domain.SituacaoUnidadeSaude}.
 */
@RestController
@RequestMapping("/api")
public class SituacaoUnidadeSaudeResource {

    private final Logger log = LoggerFactory.getLogger(SituacaoUnidadeSaudeResource.class);

    private static final String ENTITY_NAME = "situacaoUnidadeSaude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SituacaoUnidadeSaudeService situacaoUnidadeSaudeService;

    public SituacaoUnidadeSaudeResource(SituacaoUnidadeSaudeService situacaoUnidadeSaudeService) {
        this.situacaoUnidadeSaudeService = situacaoUnidadeSaudeService;
    }

    /**
     * {@code POST  /situacao-unidade-saudes} : Create a new situacaoUnidadeSaude.
     *
     * @param situacaoUnidadeSaudeDTO the situacaoUnidadeSaudeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new situacaoUnidadeSaudeDTO, or with status {@code 400 (Bad Request)} if the situacaoUnidadeSaude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/situacao-unidade-saudes")
    public ResponseEntity<SituacaoUnidadeSaudeDTO> createSituacaoUnidadeSaude(@Valid @RequestBody SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO) throws URISyntaxException {
        log.debug("REST request to save SituacaoUnidadeSaude : {}", situacaoUnidadeSaudeDTO);
        if (situacaoUnidadeSaudeDTO.getId() != null) {
            throw new BadRequestAlertException("A new situacaoUnidadeSaude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SituacaoUnidadeSaudeDTO result = situacaoUnidadeSaudeService.save(situacaoUnidadeSaudeDTO);
        return ResponseEntity.created(new URI("/api/situacao-unidade-saudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /situacao-unidade-saudes} : Updates an existing situacaoUnidadeSaude.
     *
     * @param situacaoUnidadeSaudeDTO the situacaoUnidadeSaudeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated situacaoUnidadeSaudeDTO,
     * or with status {@code 400 (Bad Request)} if the situacaoUnidadeSaudeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the situacaoUnidadeSaudeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/situacao-unidade-saudes")
    public ResponseEntity<SituacaoUnidadeSaudeDTO> updateSituacaoUnidadeSaude(@Valid @RequestBody SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO) throws URISyntaxException {
        log.debug("REST request to update SituacaoUnidadeSaude : {}", situacaoUnidadeSaudeDTO);
        if (situacaoUnidadeSaudeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SituacaoUnidadeSaudeDTO result = situacaoUnidadeSaudeService.save(situacaoUnidadeSaudeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, situacaoUnidadeSaudeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /situacao-unidade-saudes} : get all the situacaoUnidadeSaudes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of situacaoUnidadeSaudes in body.
     */
    @GetMapping("/situacao-unidade-saudes")
    public ResponseEntity<List<SituacaoUnidadeSaudeDTO>> getAllSituacaoUnidadeSaudes(Pageable pageable) {
        log.debug("REST request to get a page of SituacaoUnidadeSaudes");
        Page<SituacaoUnidadeSaudeDTO> page = situacaoUnidadeSaudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /situacao-unidade-saudes/:id} : get the "id" situacaoUnidadeSaude.
     *
     * @param id the id of the situacaoUnidadeSaudeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the situacaoUnidadeSaudeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/situacao-unidade-saudes/{id}")
    public ResponseEntity<SituacaoUnidadeSaudeDTO> getSituacaoUnidadeSaude(@PathVariable Long id) {
        log.debug("REST request to get SituacaoUnidadeSaude : {}", id);
        Optional<SituacaoUnidadeSaudeDTO> situacaoUnidadeSaudeDTO = situacaoUnidadeSaudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(situacaoUnidadeSaudeDTO);
    }

    /**
     * {@code DELETE  /situacao-unidade-saudes/:id} : delete the "id" situacaoUnidadeSaude.
     *
     * @param id the id of the situacaoUnidadeSaudeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/situacao-unidade-saudes/{id}")
    public ResponseEntity<Void> deleteSituacaoUnidadeSaude(@PathVariable Long id) {
        log.debug("REST request to delete SituacaoUnidadeSaude : {}", id);
        situacaoUnidadeSaudeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

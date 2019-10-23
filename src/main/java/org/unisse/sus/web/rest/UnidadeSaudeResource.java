package org.unisse.sus.web.rest;

import org.unisse.sus.service.UnidadeSaudeService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.UnidadeSaudeDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.UnidadeSaude}.
 */
@RestController
@RequestMapping("/api")
public class UnidadeSaudeResource {

    private final Logger log = LoggerFactory.getLogger(UnidadeSaudeResource.class);

    private static final String ENTITY_NAME = "unidadeSaude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnidadeSaudeService unidadeSaudeService;

    public UnidadeSaudeResource(UnidadeSaudeService unidadeSaudeService) {
        this.unidadeSaudeService = unidadeSaudeService;
    }

    /**
     * {@code POST  /unidade-saudes} : Create a new unidadeSaude.
     *
     * @param unidadeSaudeDTO the unidadeSaudeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unidadeSaudeDTO, or with status {@code 400 (Bad Request)} if the unidadeSaude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unidade-saudes")
    public ResponseEntity<UnidadeSaudeDTO> createUnidadeSaude(@Valid @RequestBody UnidadeSaudeDTO unidadeSaudeDTO) throws URISyntaxException {
        log.debug("REST request to save UnidadeSaude : {}", unidadeSaudeDTO);
        if (unidadeSaudeDTO.getId() != null) {
            throw new BadRequestAlertException("A new unidadeSaude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnidadeSaudeDTO result = unidadeSaudeService.save(unidadeSaudeDTO);
        return ResponseEntity.created(new URI("/api/unidade-saudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unidade-saudes} : Updates an existing unidadeSaude.
     *
     * @param unidadeSaudeDTO the unidadeSaudeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unidadeSaudeDTO,
     * or with status {@code 400 (Bad Request)} if the unidadeSaudeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unidadeSaudeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unidade-saudes")
    public ResponseEntity<UnidadeSaudeDTO> updateUnidadeSaude(@Valid @RequestBody UnidadeSaudeDTO unidadeSaudeDTO) throws URISyntaxException {
        log.debug("REST request to update UnidadeSaude : {}", unidadeSaudeDTO);
        if (unidadeSaudeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UnidadeSaudeDTO result = unidadeSaudeService.save(unidadeSaudeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unidadeSaudeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /unidade-saudes} : get all the unidadeSaudes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unidadeSaudes in body.
     */
    @GetMapping("/unidade-saudes")
    public ResponseEntity<List<UnidadeSaudeDTO>> getAllUnidadeSaudes(Pageable pageable) {
        log.debug("REST request to get a page of UnidadeSaudes");
        Page<UnidadeSaudeDTO> page = unidadeSaudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unidade-saudes/:id} : get the "id" unidadeSaude.
     *
     * @param id the id of the unidadeSaudeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unidadeSaudeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unidade-saudes/{id}")
    public ResponseEntity<UnidadeSaudeDTO> getUnidadeSaude(@PathVariable Long id) {
        log.debug("REST request to get UnidadeSaude : {}", id);
        Optional<UnidadeSaudeDTO> unidadeSaudeDTO = unidadeSaudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unidadeSaudeDTO);
    }

    /**
     * {@code DELETE  /unidade-saudes/:id} : delete the "id" unidadeSaude.
     *
     * @param id the id of the unidadeSaudeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unidade-saudes/{id}")
    public ResponseEntity<Void> deleteUnidadeSaude(@PathVariable Long id) {
        log.debug("REST request to delete UnidadeSaude : {}", id);
        unidadeSaudeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

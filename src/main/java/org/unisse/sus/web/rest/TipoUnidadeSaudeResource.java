package org.unisse.sus.web.rest;

import org.unisse.sus.service.TipoUnidadeSaudeService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.TipoUnidadeSaudeDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.TipoUnidadeSaude}.
 */
@RestController
@RequestMapping("/api")
public class TipoUnidadeSaudeResource {

    private final Logger log = LoggerFactory.getLogger(TipoUnidadeSaudeResource.class);

    private static final String ENTITY_NAME = "tipoUnidadeSaude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoUnidadeSaudeService tipoUnidadeSaudeService;

    public TipoUnidadeSaudeResource(TipoUnidadeSaudeService tipoUnidadeSaudeService) {
        this.tipoUnidadeSaudeService = tipoUnidadeSaudeService;
    }

    /**
     * {@code POST  /tipo-unidade-saudes} : Create a new tipoUnidadeSaude.
     *
     * @param tipoUnidadeSaudeDTO the tipoUnidadeSaudeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoUnidadeSaudeDTO, or with status {@code 400 (Bad Request)} if the tipoUnidadeSaude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-unidade-saudes")
    public ResponseEntity<TipoUnidadeSaudeDTO> createTipoUnidadeSaude(@Valid @RequestBody TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO) throws URISyntaxException {
        log.debug("REST request to save TipoUnidadeSaude : {}", tipoUnidadeSaudeDTO);
        if (tipoUnidadeSaudeDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoUnidadeSaude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoUnidadeSaudeDTO result = tipoUnidadeSaudeService.save(tipoUnidadeSaudeDTO);
        return ResponseEntity.created(new URI("/api/tipo-unidade-saudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-unidade-saudes} : Updates an existing tipoUnidadeSaude.
     *
     * @param tipoUnidadeSaudeDTO the tipoUnidadeSaudeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoUnidadeSaudeDTO,
     * or with status {@code 400 (Bad Request)} if the tipoUnidadeSaudeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoUnidadeSaudeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-unidade-saudes")
    public ResponseEntity<TipoUnidadeSaudeDTO> updateTipoUnidadeSaude(@Valid @RequestBody TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO) throws URISyntaxException {
        log.debug("REST request to update TipoUnidadeSaude : {}", tipoUnidadeSaudeDTO);
        if (tipoUnidadeSaudeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoUnidadeSaudeDTO result = tipoUnidadeSaudeService.save(tipoUnidadeSaudeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoUnidadeSaudeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-unidade-saudes} : get all the tipoUnidadeSaudes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoUnidadeSaudes in body.
     */
    @GetMapping("/tipo-unidade-saudes")
    public ResponseEntity<List<TipoUnidadeSaudeDTO>> getAllTipoUnidadeSaudes(Pageable pageable) {
        log.debug("REST request to get a page of TipoUnidadeSaudes");
        Page<TipoUnidadeSaudeDTO> page = tipoUnidadeSaudeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-unidade-saudes/:id} : get the "id" tipoUnidadeSaude.
     *
     * @param id the id of the tipoUnidadeSaudeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoUnidadeSaudeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-unidade-saudes/{id}")
    public ResponseEntity<TipoUnidadeSaudeDTO> getTipoUnidadeSaude(@PathVariable Long id) {
        log.debug("REST request to get TipoUnidadeSaude : {}", id);
        Optional<TipoUnidadeSaudeDTO> tipoUnidadeSaudeDTO = tipoUnidadeSaudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoUnidadeSaudeDTO);
    }

    /**
     * {@code DELETE  /tipo-unidade-saudes/:id} : delete the "id" tipoUnidadeSaude.
     *
     * @param id the id of the tipoUnidadeSaudeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-unidade-saudes/{id}")
    public ResponseEntity<Void> deleteTipoUnidadeSaude(@PathVariable Long id) {
        log.debug("REST request to delete TipoUnidadeSaude : {}", id);
        tipoUnidadeSaudeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

package org.unisse.sus.web.rest;

import org.unisse.sus.service.OpiniaoService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.OpiniaoDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.Opiniao}.
 */
@RestController
@RequestMapping("/api")
public class OpiniaoResource {

    private final Logger log = LoggerFactory.getLogger(OpiniaoResource.class);

    private static final String ENTITY_NAME = "opiniao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpiniaoService opiniaoService;

    public OpiniaoResource(OpiniaoService opiniaoService) {
        this.opiniaoService = opiniaoService;
    }

    /**
     * {@code POST  /opiniaos} : Create a new opiniao.
     *
     * @param opiniaoDTO the opiniaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opiniaoDTO, or with status {@code 400 (Bad Request)} if the opiniao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opiniaos")
    public ResponseEntity<OpiniaoDTO> createOpiniao(@Valid @RequestBody OpiniaoDTO opiniaoDTO) throws URISyntaxException {
        log.debug("REST request to save Opiniao : {}", opiniaoDTO);
        if (opiniaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new opiniao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpiniaoDTO result = opiniaoService.save(opiniaoDTO);
        return ResponseEntity.created(new URI("/api/opiniaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /opiniaos} : Updates an existing opiniao.
     *
     * @param opiniaoDTO the opiniaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opiniaoDTO,
     * or with status {@code 400 (Bad Request)} if the opiniaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opiniaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opiniaos")
    public ResponseEntity<OpiniaoDTO> updateOpiniao(@Valid @RequestBody OpiniaoDTO opiniaoDTO) throws URISyntaxException {
        log.debug("REST request to update Opiniao : {}", opiniaoDTO);
        if (opiniaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OpiniaoDTO result = opiniaoService.save(opiniaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, opiniaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /opiniaos} : get all the opiniaos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opiniaos in body.
     */
    @GetMapping("/opiniaos")
    public ResponseEntity<List<OpiniaoDTO>> getAllOpiniaos(Pageable pageable) {
        log.debug("REST request to get a page of Opiniaos");
        Page<OpiniaoDTO> page = opiniaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /opiniaos/:id} : get the "id" opiniao.
     *
     * @param id the id of the opiniaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opiniaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/opiniaos/{id}")
    public ResponseEntity<OpiniaoDTO> getOpiniao(@PathVariable Long id) {
        log.debug("REST request to get Opiniao : {}", id);
        Optional<OpiniaoDTO> opiniaoDTO = opiniaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opiniaoDTO);
    }

    /**
     * {@code DELETE  /opiniaos/:id} : delete the "id" opiniao.
     *
     * @param id the id of the opiniaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/opiniaos/{id}")
    public ResponseEntity<Void> deleteOpiniao(@PathVariable Long id) {
        log.debug("REST request to delete Opiniao : {}", id);
        opiniaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

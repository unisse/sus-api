package org.unisse.sus.web.rest;

import org.unisse.sus.service.ImportanciaService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.ImportanciaDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.Importancia}.
 */
@RestController
@RequestMapping("/api")
public class ImportanciaResource {

    private final Logger log = LoggerFactory.getLogger(ImportanciaResource.class);

    private static final String ENTITY_NAME = "importancia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImportanciaService importanciaService;

    public ImportanciaResource(ImportanciaService importanciaService) {
        this.importanciaService = importanciaService;
    }

    /**
     * {@code POST  /importancias} : Create a new importancia.
     *
     * @param importanciaDTO the importanciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new importanciaDTO, or with status {@code 400 (Bad Request)} if the importancia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/importancias")
    public ResponseEntity<ImportanciaDTO> createImportancia(@Valid @RequestBody ImportanciaDTO importanciaDTO) throws URISyntaxException {
        log.debug("REST request to save Importancia : {}", importanciaDTO);
        if (importanciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new importancia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImportanciaDTO result = importanciaService.save(importanciaDTO);
        return ResponseEntity.created(new URI("/api/importancias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /importancias} : Updates an existing importancia.
     *
     * @param importanciaDTO the importanciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importanciaDTO,
     * or with status {@code 400 (Bad Request)} if the importanciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the importanciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/importancias")
    public ResponseEntity<ImportanciaDTO> updateImportancia(@Valid @RequestBody ImportanciaDTO importanciaDTO) throws URISyntaxException {
        log.debug("REST request to update Importancia : {}", importanciaDTO);
        if (importanciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ImportanciaDTO result = importanciaService.save(importanciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, importanciaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /importancias} : get all the importancias.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of importancias in body.
     */
    @GetMapping("/importancias")
    public ResponseEntity<List<ImportanciaDTO>> getAllImportancias(Pageable pageable) {
        log.debug("REST request to get a page of Importancias");
        Page<ImportanciaDTO> page = importanciaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /importancias/:id} : get the "id" importancia.
     *
     * @param id the id of the importanciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the importanciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/importancias/{id}")
    public ResponseEntity<ImportanciaDTO> getImportancia(@PathVariable Long id) {
        log.debug("REST request to get Importancia : {}", id);
        Optional<ImportanciaDTO> importanciaDTO = importanciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(importanciaDTO);
    }

    /**
     * {@code DELETE  /importancias/:id} : delete the "id" importancia.
     *
     * @param id the id of the importanciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/importancias/{id}")
    public ResponseEntity<Void> deleteImportancia(@PathVariable Long id) {
        log.debug("REST request to delete Importancia : {}", id);
        importanciaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

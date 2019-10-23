package org.unisse.sus.web.rest;

import org.unisse.sus.service.DenunciaService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.DenunciaDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.Denuncia}.
 */
@RestController
@RequestMapping("/api")
public class DenunciaResource {

    private final Logger log = LoggerFactory.getLogger(DenunciaResource.class);

    private static final String ENTITY_NAME = "denuncia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DenunciaService denunciaService;

    public DenunciaResource(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    /**
     * {@code POST  /denuncias} : Create a new denuncia.
     *
     * @param denunciaDTO the denunciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new denunciaDTO, or with status {@code 400 (Bad Request)} if the denuncia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/denuncias")
    public ResponseEntity<DenunciaDTO> createDenuncia(@Valid @RequestBody DenunciaDTO denunciaDTO) throws URISyntaxException {
        log.debug("REST request to save Denuncia : {}", denunciaDTO);
        if (denunciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new denuncia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DenunciaDTO result = denunciaService.save(denunciaDTO);
        return ResponseEntity.created(new URI("/api/denuncias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /denuncias} : Updates an existing denuncia.
     *
     * @param denunciaDTO the denunciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated denunciaDTO,
     * or with status {@code 400 (Bad Request)} if the denunciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the denunciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/denuncias")
    public ResponseEntity<DenunciaDTO> updateDenuncia(@Valid @RequestBody DenunciaDTO denunciaDTO) throws URISyntaxException {
        log.debug("REST request to update Denuncia : {}", denunciaDTO);
        if (denunciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DenunciaDTO result = denunciaService.save(denunciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, denunciaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /denuncias} : get all the denuncias.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of denuncias in body.
     */
    @GetMapping("/denuncias")
    public ResponseEntity<List<DenunciaDTO>> getAllDenuncias(Pageable pageable) {
        log.debug("REST request to get a page of Denuncias");
        Page<DenunciaDTO> page = denunciaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /denuncias/:id} : get the "id" denuncia.
     *
     * @param id the id of the denunciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the denunciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/denuncias/{id}")
    public ResponseEntity<DenunciaDTO> getDenuncia(@PathVariable Long id) {
        log.debug("REST request to get Denuncia : {}", id);
        Optional<DenunciaDTO> denunciaDTO = denunciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(denunciaDTO);
    }

    /**
     * {@code DELETE  /denuncias/:id} : delete the "id" denuncia.
     *
     * @param id the id of the denunciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/denuncias/{id}")
    public ResponseEntity<Void> deleteDenuncia(@PathVariable Long id) {
        log.debug("REST request to delete Denuncia : {}", id);
        denunciaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

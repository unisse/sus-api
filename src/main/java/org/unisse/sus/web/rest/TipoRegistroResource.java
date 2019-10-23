package org.unisse.sus.web.rest;

import org.unisse.sus.service.TipoRegistroService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.TipoRegistroDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.TipoRegistro}.
 */
@RestController
@RequestMapping("/api")
public class TipoRegistroResource {

    private final Logger log = LoggerFactory.getLogger(TipoRegistroResource.class);

    private static final String ENTITY_NAME = "tipoRegistro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoRegistroService tipoRegistroService;

    public TipoRegistroResource(TipoRegistroService tipoRegistroService) {
        this.tipoRegistroService = tipoRegistroService;
    }

    /**
     * {@code POST  /tipo-registros} : Create a new tipoRegistro.
     *
     * @param tipoRegistroDTO the tipoRegistroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoRegistroDTO, or with status {@code 400 (Bad Request)} if the tipoRegistro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-registros")
    public ResponseEntity<TipoRegistroDTO> createTipoRegistro(@Valid @RequestBody TipoRegistroDTO tipoRegistroDTO) throws URISyntaxException {
        log.debug("REST request to save TipoRegistro : {}", tipoRegistroDTO);
        if (tipoRegistroDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoRegistro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoRegistroDTO result = tipoRegistroService.save(tipoRegistroDTO);
        return ResponseEntity.created(new URI("/api/tipo-registros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-registros} : Updates an existing tipoRegistro.
     *
     * @param tipoRegistroDTO the tipoRegistroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoRegistroDTO,
     * or with status {@code 400 (Bad Request)} if the tipoRegistroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoRegistroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-registros")
    public ResponseEntity<TipoRegistroDTO> updateTipoRegistro(@Valid @RequestBody TipoRegistroDTO tipoRegistroDTO) throws URISyntaxException {
        log.debug("REST request to update TipoRegistro : {}", tipoRegistroDTO);
        if (tipoRegistroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoRegistroDTO result = tipoRegistroService.save(tipoRegistroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoRegistroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-registros} : get all the tipoRegistros.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoRegistros in body.
     */
    @GetMapping("/tipo-registros")
    public ResponseEntity<List<TipoRegistroDTO>> getAllTipoRegistros(Pageable pageable) {
        log.debug("REST request to get a page of TipoRegistros");
        Page<TipoRegistroDTO> page = tipoRegistroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-registros/:id} : get the "id" tipoRegistro.
     *
     * @param id the id of the tipoRegistroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoRegistroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-registros/{id}")
    public ResponseEntity<TipoRegistroDTO> getTipoRegistro(@PathVariable Long id) {
        log.debug("REST request to get TipoRegistro : {}", id);
        Optional<TipoRegistroDTO> tipoRegistroDTO = tipoRegistroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoRegistroDTO);
    }

    /**
     * {@code DELETE  /tipo-registros/:id} : delete the "id" tipoRegistro.
     *
     * @param id the id of the tipoRegistroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-registros/{id}")
    public ResponseEntity<Void> deleteTipoRegistro(@PathVariable Long id) {
        log.debug("REST request to delete TipoRegistro : {}", id);
        tipoRegistroService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

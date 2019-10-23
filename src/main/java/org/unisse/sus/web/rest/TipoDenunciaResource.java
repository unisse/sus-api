package org.unisse.sus.web.rest;

import org.unisse.sus.service.TipoDenunciaService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.TipoDenunciaDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.TipoDenuncia}.
 */
@RestController
@RequestMapping("/api")
public class TipoDenunciaResource {

    private final Logger log = LoggerFactory.getLogger(TipoDenunciaResource.class);

    private static final String ENTITY_NAME = "tipoDenuncia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoDenunciaService tipoDenunciaService;

    public TipoDenunciaResource(TipoDenunciaService tipoDenunciaService) {
        this.tipoDenunciaService = tipoDenunciaService;
    }

    /**
     * {@code POST  /tipo-denuncias} : Create a new tipoDenuncia.
     *
     * @param tipoDenunciaDTO the tipoDenunciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoDenunciaDTO, or with status {@code 400 (Bad Request)} if the tipoDenuncia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-denuncias")
    public ResponseEntity<TipoDenunciaDTO> createTipoDenuncia(@Valid @RequestBody TipoDenunciaDTO tipoDenunciaDTO) throws URISyntaxException {
        log.debug("REST request to save TipoDenuncia : {}", tipoDenunciaDTO);
        if (tipoDenunciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoDenuncia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoDenunciaDTO result = tipoDenunciaService.save(tipoDenunciaDTO);
        return ResponseEntity.created(new URI("/api/tipo-denuncias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-denuncias} : Updates an existing tipoDenuncia.
     *
     * @param tipoDenunciaDTO the tipoDenunciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoDenunciaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoDenunciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoDenunciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-denuncias")
    public ResponseEntity<TipoDenunciaDTO> updateTipoDenuncia(@Valid @RequestBody TipoDenunciaDTO tipoDenunciaDTO) throws URISyntaxException {
        log.debug("REST request to update TipoDenuncia : {}", tipoDenunciaDTO);
        if (tipoDenunciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoDenunciaDTO result = tipoDenunciaService.save(tipoDenunciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoDenunciaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-denuncias} : get all the tipoDenuncias.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoDenuncias in body.
     */
    @GetMapping("/tipo-denuncias")
    public ResponseEntity<List<TipoDenunciaDTO>> getAllTipoDenuncias(Pageable pageable) {
        log.debug("REST request to get a page of TipoDenuncias");
        Page<TipoDenunciaDTO> page = tipoDenunciaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-denuncias/:id} : get the "id" tipoDenuncia.
     *
     * @param id the id of the tipoDenunciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoDenunciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-denuncias/{id}")
    public ResponseEntity<TipoDenunciaDTO> getTipoDenuncia(@PathVariable Long id) {
        log.debug("REST request to get TipoDenuncia : {}", id);
        Optional<TipoDenunciaDTO> tipoDenunciaDTO = tipoDenunciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoDenunciaDTO);
    }

    /**
     * {@code DELETE  /tipo-denuncias/:id} : delete the "id" tipoDenuncia.
     *
     * @param id the id of the tipoDenunciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-denuncias/{id}")
    public ResponseEntity<Void> deleteTipoDenuncia(@PathVariable Long id) {
        log.debug("REST request to delete TipoDenuncia : {}", id);
        tipoDenunciaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

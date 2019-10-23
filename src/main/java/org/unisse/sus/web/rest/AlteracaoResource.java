package org.unisse.sus.web.rest;

import org.unisse.sus.service.AlteracaoService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.AlteracaoDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.Alteracao}.
 */
@RestController
@RequestMapping("/api")
public class AlteracaoResource {

    private final Logger log = LoggerFactory.getLogger(AlteracaoResource.class);

    private static final String ENTITY_NAME = "alteracao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlteracaoService alteracaoService;

    public AlteracaoResource(AlteracaoService alteracaoService) {
        this.alteracaoService = alteracaoService;
    }

    /**
     * {@code POST  /alteracaos} : Create a new alteracao.
     *
     * @param alteracaoDTO the alteracaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alteracaoDTO, or with status {@code 400 (Bad Request)} if the alteracao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alteracaos")
    public ResponseEntity<AlteracaoDTO> createAlteracao(@Valid @RequestBody AlteracaoDTO alteracaoDTO) throws URISyntaxException {
        log.debug("REST request to save Alteracao : {}", alteracaoDTO);
        if (alteracaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new alteracao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlteracaoDTO result = alteracaoService.save(alteracaoDTO);
        return ResponseEntity.created(new URI("/api/alteracaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alteracaos} : Updates an existing alteracao.
     *
     * @param alteracaoDTO the alteracaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alteracaoDTO,
     * or with status {@code 400 (Bad Request)} if the alteracaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alteracaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alteracaos")
    public ResponseEntity<AlteracaoDTO> updateAlteracao(@Valid @RequestBody AlteracaoDTO alteracaoDTO) throws URISyntaxException {
        log.debug("REST request to update Alteracao : {}", alteracaoDTO);
        if (alteracaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlteracaoDTO result = alteracaoService.save(alteracaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alteracaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alteracaos} : get all the alteracaos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alteracaos in body.
     */
    @GetMapping("/alteracaos")
    public ResponseEntity<List<AlteracaoDTO>> getAllAlteracaos(Pageable pageable) {
        log.debug("REST request to get a page of Alteracaos");
        Page<AlteracaoDTO> page = alteracaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alteracaos/:id} : get the "id" alteracao.
     *
     * @param id the id of the alteracaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alteracaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alteracaos/{id}")
    public ResponseEntity<AlteracaoDTO> getAlteracao(@PathVariable Long id) {
        log.debug("REST request to get Alteracao : {}", id);
        Optional<AlteracaoDTO> alteracaoDTO = alteracaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alteracaoDTO);
    }

    /**
     * {@code DELETE  /alteracaos/:id} : delete the "id" alteracao.
     *
     * @param id the id of the alteracaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alteracaos/{id}")
    public ResponseEntity<Void> deleteAlteracao(@PathVariable Long id) {
        log.debug("REST request to delete Alteracao : {}", id);
        alteracaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

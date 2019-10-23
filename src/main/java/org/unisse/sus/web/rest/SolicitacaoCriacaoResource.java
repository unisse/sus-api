package org.unisse.sus.web.rest;

import org.unisse.sus.service.SolicitacaoCriacaoService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.SolicitacaoCriacaoDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.SolicitacaoCriacao}.
 */
@RestController
@RequestMapping("/api")
public class SolicitacaoCriacaoResource {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoCriacaoResource.class);

    private static final String ENTITY_NAME = "solicitacaoCriacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolicitacaoCriacaoService solicitacaoCriacaoService;

    public SolicitacaoCriacaoResource(SolicitacaoCriacaoService solicitacaoCriacaoService) {
        this.solicitacaoCriacaoService = solicitacaoCriacaoService;
    }

    /**
     * {@code POST  /solicitacao-criacaos} : Create a new solicitacaoCriacao.
     *
     * @param solicitacaoCriacaoDTO the solicitacaoCriacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solicitacaoCriacaoDTO, or with status {@code 400 (Bad Request)} if the solicitacaoCriacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/solicitacao-criacaos")
    public ResponseEntity<SolicitacaoCriacaoDTO> createSolicitacaoCriacao(@Valid @RequestBody SolicitacaoCriacaoDTO solicitacaoCriacaoDTO) throws URISyntaxException {
        log.debug("REST request to save SolicitacaoCriacao : {}", solicitacaoCriacaoDTO);
        if (solicitacaoCriacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new solicitacaoCriacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolicitacaoCriacaoDTO result = solicitacaoCriacaoService.save(solicitacaoCriacaoDTO);
        return ResponseEntity.created(new URI("/api/solicitacao-criacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solicitacao-criacaos} : Updates an existing solicitacaoCriacao.
     *
     * @param solicitacaoCriacaoDTO the solicitacaoCriacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitacaoCriacaoDTO,
     * or with status {@code 400 (Bad Request)} if the solicitacaoCriacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solicitacaoCriacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/solicitacao-criacaos")
    public ResponseEntity<SolicitacaoCriacaoDTO> updateSolicitacaoCriacao(@Valid @RequestBody SolicitacaoCriacaoDTO solicitacaoCriacaoDTO) throws URISyntaxException {
        log.debug("REST request to update SolicitacaoCriacao : {}", solicitacaoCriacaoDTO);
        if (solicitacaoCriacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SolicitacaoCriacaoDTO result = solicitacaoCriacaoService.save(solicitacaoCriacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solicitacaoCriacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /solicitacao-criacaos} : get all the solicitacaoCriacaos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solicitacaoCriacaos in body.
     */
    @GetMapping("/solicitacao-criacaos")
    public ResponseEntity<List<SolicitacaoCriacaoDTO>> getAllSolicitacaoCriacaos(Pageable pageable) {
        log.debug("REST request to get a page of SolicitacaoCriacaos");
        Page<SolicitacaoCriacaoDTO> page = solicitacaoCriacaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /solicitacao-criacaos/:id} : get the "id" solicitacaoCriacao.
     *
     * @param id the id of the solicitacaoCriacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solicitacaoCriacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/solicitacao-criacaos/{id}")
    public ResponseEntity<SolicitacaoCriacaoDTO> getSolicitacaoCriacao(@PathVariable Long id) {
        log.debug("REST request to get SolicitacaoCriacao : {}", id);
        Optional<SolicitacaoCriacaoDTO> solicitacaoCriacaoDTO = solicitacaoCriacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solicitacaoCriacaoDTO);
    }

    /**
     * {@code DELETE  /solicitacao-criacaos/:id} : delete the "id" solicitacaoCriacao.
     *
     * @param id the id of the solicitacaoCriacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/solicitacao-criacaos/{id}")
    public ResponseEntity<Void> deleteSolicitacaoCriacao(@PathVariable Long id) {
        log.debug("REST request to delete SolicitacaoCriacao : {}", id);
        solicitacaoCriacaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

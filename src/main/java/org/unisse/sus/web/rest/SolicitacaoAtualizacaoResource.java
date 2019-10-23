package org.unisse.sus.web.rest;

import org.unisse.sus.service.SolicitacaoAtualizacaoService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.SolicitacaoAtualizacaoDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.SolicitacaoAtualizacao}.
 */
@RestController
@RequestMapping("/api")
public class SolicitacaoAtualizacaoResource {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoAtualizacaoResource.class);

    private static final String ENTITY_NAME = "solicitacaoAtualizacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolicitacaoAtualizacaoService solicitacaoAtualizacaoService;

    public SolicitacaoAtualizacaoResource(SolicitacaoAtualizacaoService solicitacaoAtualizacaoService) {
        this.solicitacaoAtualizacaoService = solicitacaoAtualizacaoService;
    }

    /**
     * {@code POST  /solicitacao-atualizacaos} : Create a new solicitacaoAtualizacao.
     *
     * @param solicitacaoAtualizacaoDTO the solicitacaoAtualizacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solicitacaoAtualizacaoDTO, or with status {@code 400 (Bad Request)} if the solicitacaoAtualizacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/solicitacao-atualizacaos")
    public ResponseEntity<SolicitacaoAtualizacaoDTO> createSolicitacaoAtualizacao(@Valid @RequestBody SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO) throws URISyntaxException {
        log.debug("REST request to save SolicitacaoAtualizacao : {}", solicitacaoAtualizacaoDTO);
        if (solicitacaoAtualizacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new solicitacaoAtualizacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolicitacaoAtualizacaoDTO result = solicitacaoAtualizacaoService.save(solicitacaoAtualizacaoDTO);
        return ResponseEntity.created(new URI("/api/solicitacao-atualizacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solicitacao-atualizacaos} : Updates an existing solicitacaoAtualizacao.
     *
     * @param solicitacaoAtualizacaoDTO the solicitacaoAtualizacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitacaoAtualizacaoDTO,
     * or with status {@code 400 (Bad Request)} if the solicitacaoAtualizacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solicitacaoAtualizacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/solicitacao-atualizacaos")
    public ResponseEntity<SolicitacaoAtualizacaoDTO> updateSolicitacaoAtualizacao(@Valid @RequestBody SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO) throws URISyntaxException {
        log.debug("REST request to update SolicitacaoAtualizacao : {}", solicitacaoAtualizacaoDTO);
        if (solicitacaoAtualizacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SolicitacaoAtualizacaoDTO result = solicitacaoAtualizacaoService.save(solicitacaoAtualizacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solicitacaoAtualizacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /solicitacao-atualizacaos} : get all the solicitacaoAtualizacaos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solicitacaoAtualizacaos in body.
     */
    @GetMapping("/solicitacao-atualizacaos")
    public ResponseEntity<List<SolicitacaoAtualizacaoDTO>> getAllSolicitacaoAtualizacaos(Pageable pageable) {
        log.debug("REST request to get a page of SolicitacaoAtualizacaos");
        Page<SolicitacaoAtualizacaoDTO> page = solicitacaoAtualizacaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /solicitacao-atualizacaos/:id} : get the "id" solicitacaoAtualizacao.
     *
     * @param id the id of the solicitacaoAtualizacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solicitacaoAtualizacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/solicitacao-atualizacaos/{id}")
    public ResponseEntity<SolicitacaoAtualizacaoDTO> getSolicitacaoAtualizacao(@PathVariable Long id) {
        log.debug("REST request to get SolicitacaoAtualizacao : {}", id);
        Optional<SolicitacaoAtualizacaoDTO> solicitacaoAtualizacaoDTO = solicitacaoAtualizacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solicitacaoAtualizacaoDTO);
    }

    /**
     * {@code DELETE  /solicitacao-atualizacaos/:id} : delete the "id" solicitacaoAtualizacao.
     *
     * @param id the id of the solicitacaoAtualizacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/solicitacao-atualizacaos/{id}")
    public ResponseEntity<Void> deleteSolicitacaoAtualizacao(@PathVariable Long id) {
        log.debug("REST request to delete SolicitacaoAtualizacao : {}", id);
        solicitacaoAtualizacaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

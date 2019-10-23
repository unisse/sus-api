package org.unisse.sus.web.rest;

import org.unisse.sus.service.ComentarioService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.ComentarioDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.Comentario}.
 */
@RestController
@RequestMapping("/api")
public class ComentarioResource {

    private final Logger log = LoggerFactory.getLogger(ComentarioResource.class);

    private static final String ENTITY_NAME = "comentario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComentarioService comentarioService;

    public ComentarioResource(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    /**
     * {@code POST  /comentarios} : Create a new comentario.
     *
     * @param comentarioDTO the comentarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comentarioDTO, or with status {@code 400 (Bad Request)} if the comentario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comentarios")
    public ResponseEntity<ComentarioDTO> createComentario(@Valid @RequestBody ComentarioDTO comentarioDTO) throws URISyntaxException {
        log.debug("REST request to save Comentario : {}", comentarioDTO);
        if (comentarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new comentario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComentarioDTO result = comentarioService.save(comentarioDTO);
        return ResponseEntity.created(new URI("/api/comentarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comentarios} : Updates an existing comentario.
     *
     * @param comentarioDTO the comentarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentarioDTO,
     * or with status {@code 400 (Bad Request)} if the comentarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comentarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comentarios")
    public ResponseEntity<ComentarioDTO> updateComentario(@Valid @RequestBody ComentarioDTO comentarioDTO) throws URISyntaxException {
        log.debug("REST request to update Comentario : {}", comentarioDTO);
        if (comentarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ComentarioDTO result = comentarioService.save(comentarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comentarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comentarios} : get all the comentarios.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comentarios in body.
     */
    @GetMapping("/comentarios")
    public ResponseEntity<List<ComentarioDTO>> getAllComentarios(Pageable pageable) {
        log.debug("REST request to get a page of Comentarios");
        Page<ComentarioDTO> page = comentarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comentarios/:id} : get the "id" comentario.
     *
     * @param id the id of the comentarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comentarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comentarios/{id}")
    public ResponseEntity<ComentarioDTO> getComentario(@PathVariable Long id) {
        log.debug("REST request to get Comentario : {}", id);
        Optional<ComentarioDTO> comentarioDTO = comentarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comentarioDTO);
    }

    /**
     * {@code DELETE  /comentarios/:id} : delete the "id" comentario.
     *
     * @param id the id of the comentarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        log.debug("REST request to delete Comentario : {}", id);
        comentarioService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

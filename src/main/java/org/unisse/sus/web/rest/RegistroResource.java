package org.unisse.sus.web.rest;

import org.unisse.sus.service.RegistroService;
import org.unisse.sus.web.rest.errors.BadRequestAlertException;
import org.unisse.sus.service.dto.RegistroDTO;

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
 * REST controller for managing {@link org.unisse.sus.domain.Registro}.
 */
@RestController
@RequestMapping("/api")
public class RegistroResource {

    private final Logger log = LoggerFactory.getLogger(RegistroResource.class);

    private static final String ENTITY_NAME = "registro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroService registroService;

    public RegistroResource(RegistroService registroService) {
        this.registroService = registroService;
    }

    /**
     * {@code POST  /registros} : Create a new registro.
     *
     * @param registroDTO the registroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroDTO, or with status {@code 400 (Bad Request)} if the registro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registros")
    public ResponseEntity<RegistroDTO> createRegistro(@Valid @RequestBody RegistroDTO registroDTO) throws URISyntaxException {
        log.debug("REST request to save Registro : {}", registroDTO);
        if (registroDTO.getId() != null) {
            throw new BadRequestAlertException("A new registro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistroDTO result = registroService.save(registroDTO);
        return ResponseEntity.created(new URI("/api/registros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registros} : Updates an existing registro.
     *
     * @param registroDTO the registroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDTO,
     * or with status {@code 400 (Bad Request)} if the registroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registros")
    public ResponseEntity<RegistroDTO> updateRegistro(@Valid @RequestBody RegistroDTO registroDTO) throws URISyntaxException {
        log.debug("REST request to update Registro : {}", registroDTO);
        if (registroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegistroDTO result = registroService.save(registroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, registroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /registros} : get all the registros.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registros in body.
     */
    @GetMapping("/registros")
    public ResponseEntity<List<RegistroDTO>> getAllRegistros(Pageable pageable) {
        log.debug("REST request to get a page of Registros");
        Page<RegistroDTO> page = registroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registros/:id} : get the "id" registro.
     *
     * @param id the id of the registroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registros/{id}")
    public ResponseEntity<RegistroDTO> getRegistro(@PathVariable Long id) {
        log.debug("REST request to get Registro : {}", id);
        Optional<RegistroDTO> registroDTO = registroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registroDTO);
    }

    /**
     * {@code DELETE  /registros/:id} : delete the "id" registro.
     *
     * @param id the id of the registroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registros/{id}")
    public ResponseEntity<Void> deleteRegistro(@PathVariable Long id) {
        log.debug("REST request to delete Registro : {}", id);
        registroService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

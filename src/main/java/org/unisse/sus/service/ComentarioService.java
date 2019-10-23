package org.unisse.sus.service;

import org.unisse.sus.service.dto.ComentarioDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.Comentario}.
 */
public interface ComentarioService {

    /**
     * Save a comentario.
     *
     * @param comentarioDTO the entity to save.
     * @return the persisted entity.
     */
    ComentarioDTO save(ComentarioDTO comentarioDTO);

    /**
     * Get all the comentarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComentarioDTO> findAll(Pageable pageable);


    /**
     * Get the "id" comentario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComentarioDTO> findOne(Long id);

    /**
     * Delete the "id" comentario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

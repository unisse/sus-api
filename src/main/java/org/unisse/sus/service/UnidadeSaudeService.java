package org.unisse.sus.service;

import org.unisse.sus.service.dto.UnidadeSaudeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.UnidadeSaude}.
 */
public interface UnidadeSaudeService {

    /**
     * Save a unidadeSaude.
     *
     * @param unidadeSaudeDTO the entity to save.
     * @return the persisted entity.
     */
    UnidadeSaudeDTO save(UnidadeSaudeDTO unidadeSaudeDTO);

    /**
     * Get all the unidadeSaudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UnidadeSaudeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" unidadeSaude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnidadeSaudeDTO> findOne(Long id);

    /**
     * Delete the "id" unidadeSaude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

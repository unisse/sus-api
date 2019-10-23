package org.unisse.sus.service;

import org.unisse.sus.service.dto.TipoUnidadeSaudeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.TipoUnidadeSaude}.
 */
public interface TipoUnidadeSaudeService {

    /**
     * Save a tipoUnidadeSaude.
     *
     * @param tipoUnidadeSaudeDTO the entity to save.
     * @return the persisted entity.
     */
    TipoUnidadeSaudeDTO save(TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO);

    /**
     * Get all the tipoUnidadeSaudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoUnidadeSaudeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" tipoUnidadeSaude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoUnidadeSaudeDTO> findOne(Long id);

    /**
     * Delete the "id" tipoUnidadeSaude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package org.unisse.sus.service;

import org.unisse.sus.service.dto.SituacaoUnidadeSaudeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.SituacaoUnidadeSaude}.
 */
public interface SituacaoUnidadeSaudeService {

    /**
     * Save a situacaoUnidadeSaude.
     *
     * @param situacaoUnidadeSaudeDTO the entity to save.
     * @return the persisted entity.
     */
    SituacaoUnidadeSaudeDTO save(SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO);

    /**
     * Get all the situacaoUnidadeSaudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SituacaoUnidadeSaudeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" situacaoUnidadeSaude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SituacaoUnidadeSaudeDTO> findOne(Long id);

    /**
     * Delete the "id" situacaoUnidadeSaude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

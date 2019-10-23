package org.unisse.sus.service;

import org.unisse.sus.service.dto.AlteracaoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.Alteracao}.
 */
public interface AlteracaoService {

    /**
     * Save a alteracao.
     *
     * @param alteracaoDTO the entity to save.
     * @return the persisted entity.
     */
    AlteracaoDTO save(AlteracaoDTO alteracaoDTO);

    /**
     * Get all the alteracaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlteracaoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" alteracao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlteracaoDTO> findOne(Long id);

    /**
     * Delete the "id" alteracao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

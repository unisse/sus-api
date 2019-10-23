package org.unisse.sus.service;

import org.unisse.sus.service.dto.OpiniaoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.Opiniao}.
 */
public interface OpiniaoService {

    /**
     * Save a opiniao.
     *
     * @param opiniaoDTO the entity to save.
     * @return the persisted entity.
     */
    OpiniaoDTO save(OpiniaoDTO opiniaoDTO);

    /**
     * Get all the opiniaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OpiniaoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" opiniao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OpiniaoDTO> findOne(Long id);

    /**
     * Delete the "id" opiniao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

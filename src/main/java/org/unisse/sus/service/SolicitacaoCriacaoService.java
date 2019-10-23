package org.unisse.sus.service;

import org.unisse.sus.service.dto.SolicitacaoCriacaoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.SolicitacaoCriacao}.
 */
public interface SolicitacaoCriacaoService {

    /**
     * Save a solicitacaoCriacao.
     *
     * @param solicitacaoCriacaoDTO the entity to save.
     * @return the persisted entity.
     */
    SolicitacaoCriacaoDTO save(SolicitacaoCriacaoDTO solicitacaoCriacaoDTO);

    /**
     * Get all the solicitacaoCriacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolicitacaoCriacaoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" solicitacaoCriacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SolicitacaoCriacaoDTO> findOne(Long id);

    /**
     * Delete the "id" solicitacaoCriacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

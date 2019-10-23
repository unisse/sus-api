package org.unisse.sus.service;

import org.unisse.sus.service.dto.SolicitacaoAtualizacaoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.SolicitacaoAtualizacao}.
 */
public interface SolicitacaoAtualizacaoService {

    /**
     * Save a solicitacaoAtualizacao.
     *
     * @param solicitacaoAtualizacaoDTO the entity to save.
     * @return the persisted entity.
     */
    SolicitacaoAtualizacaoDTO save(SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO);

    /**
     * Get all the solicitacaoAtualizacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SolicitacaoAtualizacaoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" solicitacaoAtualizacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SolicitacaoAtualizacaoDTO> findOne(Long id);

    /**
     * Delete the "id" solicitacaoAtualizacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

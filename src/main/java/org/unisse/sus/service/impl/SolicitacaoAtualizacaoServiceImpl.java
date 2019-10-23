package org.unisse.sus.service.impl;

import org.unisse.sus.service.SolicitacaoAtualizacaoService;
import org.unisse.sus.domain.SolicitacaoAtualizacao;
import org.unisse.sus.repository.SolicitacaoAtualizacaoRepository;
import org.unisse.sus.service.dto.SolicitacaoAtualizacaoDTO;
import org.unisse.sus.service.mapper.SolicitacaoAtualizacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SolicitacaoAtualizacao}.
 */
@Service
@Transactional
public class SolicitacaoAtualizacaoServiceImpl implements SolicitacaoAtualizacaoService {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoAtualizacaoServiceImpl.class);

    private final SolicitacaoAtualizacaoRepository solicitacaoAtualizacaoRepository;

    private final SolicitacaoAtualizacaoMapper solicitacaoAtualizacaoMapper;

    public SolicitacaoAtualizacaoServiceImpl(SolicitacaoAtualizacaoRepository solicitacaoAtualizacaoRepository, SolicitacaoAtualizacaoMapper solicitacaoAtualizacaoMapper) {
        this.solicitacaoAtualizacaoRepository = solicitacaoAtualizacaoRepository;
        this.solicitacaoAtualizacaoMapper = solicitacaoAtualizacaoMapper;
    }

    /**
     * Save a solicitacaoAtualizacao.
     *
     * @param solicitacaoAtualizacaoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SolicitacaoAtualizacaoDTO save(SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO) {
        log.debug("Request to save SolicitacaoAtualizacao : {}", solicitacaoAtualizacaoDTO);
        SolicitacaoAtualizacao solicitacaoAtualizacao = solicitacaoAtualizacaoMapper.toEntity(solicitacaoAtualizacaoDTO);
        solicitacaoAtualizacao = solicitacaoAtualizacaoRepository.save(solicitacaoAtualizacao);
        return solicitacaoAtualizacaoMapper.toDto(solicitacaoAtualizacao);
    }

    /**
     * Get all the solicitacaoAtualizacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoAtualizacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SolicitacaoAtualizacaos");
        return solicitacaoAtualizacaoRepository.findAll(pageable)
            .map(solicitacaoAtualizacaoMapper::toDto);
    }


    /**
     * Get one solicitacaoAtualizacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitacaoAtualizacaoDTO> findOne(Long id) {
        log.debug("Request to get SolicitacaoAtualizacao : {}", id);
        return solicitacaoAtualizacaoRepository.findById(id)
            .map(solicitacaoAtualizacaoMapper::toDto);
    }

    /**
     * Delete the solicitacaoAtualizacao by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SolicitacaoAtualizacao : {}", id);
        solicitacaoAtualizacaoRepository.deleteById(id);
    }
}

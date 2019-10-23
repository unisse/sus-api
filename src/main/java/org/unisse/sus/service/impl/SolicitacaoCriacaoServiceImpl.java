package org.unisse.sus.service.impl;

import org.unisse.sus.service.SolicitacaoCriacaoService;
import org.unisse.sus.domain.SolicitacaoCriacao;
import org.unisse.sus.repository.SolicitacaoCriacaoRepository;
import org.unisse.sus.service.dto.SolicitacaoCriacaoDTO;
import org.unisse.sus.service.mapper.SolicitacaoCriacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SolicitacaoCriacao}.
 */
@Service
@Transactional
public class SolicitacaoCriacaoServiceImpl implements SolicitacaoCriacaoService {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoCriacaoServiceImpl.class);

    private final SolicitacaoCriacaoRepository solicitacaoCriacaoRepository;

    private final SolicitacaoCriacaoMapper solicitacaoCriacaoMapper;

    public SolicitacaoCriacaoServiceImpl(SolicitacaoCriacaoRepository solicitacaoCriacaoRepository, SolicitacaoCriacaoMapper solicitacaoCriacaoMapper) {
        this.solicitacaoCriacaoRepository = solicitacaoCriacaoRepository;
        this.solicitacaoCriacaoMapper = solicitacaoCriacaoMapper;
    }

    /**
     * Save a solicitacaoCriacao.
     *
     * @param solicitacaoCriacaoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SolicitacaoCriacaoDTO save(SolicitacaoCriacaoDTO solicitacaoCriacaoDTO) {
        log.debug("Request to save SolicitacaoCriacao : {}", solicitacaoCriacaoDTO);
        SolicitacaoCriacao solicitacaoCriacao = solicitacaoCriacaoMapper.toEntity(solicitacaoCriacaoDTO);
        solicitacaoCriacao = solicitacaoCriacaoRepository.save(solicitacaoCriacao);
        return solicitacaoCriacaoMapper.toDto(solicitacaoCriacao);
    }

    /**
     * Get all the solicitacaoCriacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoCriacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SolicitacaoCriacaos");
        return solicitacaoCriacaoRepository.findAll(pageable)
            .map(solicitacaoCriacaoMapper::toDto);
    }


    /**
     * Get one solicitacaoCriacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitacaoCriacaoDTO> findOne(Long id) {
        log.debug("Request to get SolicitacaoCriacao : {}", id);
        return solicitacaoCriacaoRepository.findById(id)
            .map(solicitacaoCriacaoMapper::toDto);
    }

    /**
     * Delete the solicitacaoCriacao by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SolicitacaoCriacao : {}", id);
        solicitacaoCriacaoRepository.deleteById(id);
    }
}

package org.unisse.sus.service.impl;

import org.unisse.sus.service.AlteracaoService;
import org.unisse.sus.domain.Alteracao;
import org.unisse.sus.repository.AlteracaoRepository;
import org.unisse.sus.service.dto.AlteracaoDTO;
import org.unisse.sus.service.mapper.AlteracaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Alteracao}.
 */
@Service
@Transactional
public class AlteracaoServiceImpl implements AlteracaoService {

    private final Logger log = LoggerFactory.getLogger(AlteracaoServiceImpl.class);

    private final AlteracaoRepository alteracaoRepository;

    private final AlteracaoMapper alteracaoMapper;

    public AlteracaoServiceImpl(AlteracaoRepository alteracaoRepository, AlteracaoMapper alteracaoMapper) {
        this.alteracaoRepository = alteracaoRepository;
        this.alteracaoMapper = alteracaoMapper;
    }

    /**
     * Save a alteracao.
     *
     * @param alteracaoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AlteracaoDTO save(AlteracaoDTO alteracaoDTO) {
        log.debug("Request to save Alteracao : {}", alteracaoDTO);
        Alteracao alteracao = alteracaoMapper.toEntity(alteracaoDTO);
        alteracao = alteracaoRepository.save(alteracao);
        return alteracaoMapper.toDto(alteracao);
    }

    /**
     * Get all the alteracaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AlteracaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Alteracaos");
        return alteracaoRepository.findAll(pageable)
            .map(alteracaoMapper::toDto);
    }


    /**
     * Get one alteracao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AlteracaoDTO> findOne(Long id) {
        log.debug("Request to get Alteracao : {}", id);
        return alteracaoRepository.findById(id)
            .map(alteracaoMapper::toDto);
    }

    /**
     * Delete the alteracao by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alteracao : {}", id);
        alteracaoRepository.deleteById(id);
    }
}

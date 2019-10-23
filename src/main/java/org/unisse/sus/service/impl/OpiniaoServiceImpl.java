package org.unisse.sus.service.impl;

import org.unisse.sus.service.OpiniaoService;
import org.unisse.sus.domain.Opiniao;
import org.unisse.sus.repository.OpiniaoRepository;
import org.unisse.sus.service.dto.OpiniaoDTO;
import org.unisse.sus.service.mapper.OpiniaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Opiniao}.
 */
@Service
@Transactional
public class OpiniaoServiceImpl implements OpiniaoService {

    private final Logger log = LoggerFactory.getLogger(OpiniaoServiceImpl.class);

    private final OpiniaoRepository opiniaoRepository;

    private final OpiniaoMapper opiniaoMapper;

    public OpiniaoServiceImpl(OpiniaoRepository opiniaoRepository, OpiniaoMapper opiniaoMapper) {
        this.opiniaoRepository = opiniaoRepository;
        this.opiniaoMapper = opiniaoMapper;
    }

    /**
     * Save a opiniao.
     *
     * @param opiniaoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OpiniaoDTO save(OpiniaoDTO opiniaoDTO) {
        log.debug("Request to save Opiniao : {}", opiniaoDTO);
        Opiniao opiniao = opiniaoMapper.toEntity(opiniaoDTO);
        opiniao = opiniaoRepository.save(opiniao);
        return opiniaoMapper.toDto(opiniao);
    }

    /**
     * Get all the opiniaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OpiniaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Opiniaos");
        return opiniaoRepository.findAll(pageable)
            .map(opiniaoMapper::toDto);
    }


    /**
     * Get one opiniao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OpiniaoDTO> findOne(Long id) {
        log.debug("Request to get Opiniao : {}", id);
        return opiniaoRepository.findById(id)
            .map(opiniaoMapper::toDto);
    }

    /**
     * Delete the opiniao by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Opiniao : {}", id);
        opiniaoRepository.deleteById(id);
    }
}

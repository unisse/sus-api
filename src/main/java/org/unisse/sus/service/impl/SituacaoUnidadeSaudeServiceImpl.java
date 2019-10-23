package org.unisse.sus.service.impl;

import org.unisse.sus.service.SituacaoUnidadeSaudeService;
import org.unisse.sus.domain.SituacaoUnidadeSaude;
import org.unisse.sus.repository.SituacaoUnidadeSaudeRepository;
import org.unisse.sus.service.dto.SituacaoUnidadeSaudeDTO;
import org.unisse.sus.service.mapper.SituacaoUnidadeSaudeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SituacaoUnidadeSaude}.
 */
@Service
@Transactional
public class SituacaoUnidadeSaudeServiceImpl implements SituacaoUnidadeSaudeService {

    private final Logger log = LoggerFactory.getLogger(SituacaoUnidadeSaudeServiceImpl.class);

    private final SituacaoUnidadeSaudeRepository situacaoUnidadeSaudeRepository;

    private final SituacaoUnidadeSaudeMapper situacaoUnidadeSaudeMapper;

    public SituacaoUnidadeSaudeServiceImpl(SituacaoUnidadeSaudeRepository situacaoUnidadeSaudeRepository, SituacaoUnidadeSaudeMapper situacaoUnidadeSaudeMapper) {
        this.situacaoUnidadeSaudeRepository = situacaoUnidadeSaudeRepository;
        this.situacaoUnidadeSaudeMapper = situacaoUnidadeSaudeMapper;
    }

    /**
     * Save a situacaoUnidadeSaude.
     *
     * @param situacaoUnidadeSaudeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SituacaoUnidadeSaudeDTO save(SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO) {
        log.debug("Request to save SituacaoUnidadeSaude : {}", situacaoUnidadeSaudeDTO);
        SituacaoUnidadeSaude situacaoUnidadeSaude = situacaoUnidadeSaudeMapper.toEntity(situacaoUnidadeSaudeDTO);
        situacaoUnidadeSaude = situacaoUnidadeSaudeRepository.save(situacaoUnidadeSaude);
        return situacaoUnidadeSaudeMapper.toDto(situacaoUnidadeSaude);
    }

    /**
     * Get all the situacaoUnidadeSaudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SituacaoUnidadeSaudeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SituacaoUnidadeSaudes");
        return situacaoUnidadeSaudeRepository.findAll(pageable)
            .map(situacaoUnidadeSaudeMapper::toDto);
    }


    /**
     * Get one situacaoUnidadeSaude by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SituacaoUnidadeSaudeDTO> findOne(Long id) {
        log.debug("Request to get SituacaoUnidadeSaude : {}", id);
        return situacaoUnidadeSaudeRepository.findById(id)
            .map(situacaoUnidadeSaudeMapper::toDto);
    }

    /**
     * Delete the situacaoUnidadeSaude by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SituacaoUnidadeSaude : {}", id);
        situacaoUnidadeSaudeRepository.deleteById(id);
    }
}

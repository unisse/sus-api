package org.unisse.sus.service.impl;

import org.unisse.sus.service.UnidadeSaudeService;
import org.unisse.sus.domain.UnidadeSaude;
import org.unisse.sus.repository.UnidadeSaudeRepository;
import org.unisse.sus.service.dto.UnidadeSaudeDTO;
import org.unisse.sus.service.mapper.UnidadeSaudeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UnidadeSaude}.
 */
@Service
@Transactional
public class UnidadeSaudeServiceImpl implements UnidadeSaudeService {

    private final Logger log = LoggerFactory.getLogger(UnidadeSaudeServiceImpl.class);

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    private final UnidadeSaudeMapper unidadeSaudeMapper;

    public UnidadeSaudeServiceImpl(UnidadeSaudeRepository unidadeSaudeRepository, UnidadeSaudeMapper unidadeSaudeMapper) {
        this.unidadeSaudeRepository = unidadeSaudeRepository;
        this.unidadeSaudeMapper = unidadeSaudeMapper;
    }

    /**
     * Save a unidadeSaude.
     *
     * @param unidadeSaudeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UnidadeSaudeDTO save(UnidadeSaudeDTO unidadeSaudeDTO) {
        log.debug("Request to save UnidadeSaude : {}", unidadeSaudeDTO);
        UnidadeSaude unidadeSaude = unidadeSaudeMapper.toEntity(unidadeSaudeDTO);
        unidadeSaude = unidadeSaudeRepository.save(unidadeSaude);
        return unidadeSaudeMapper.toDto(unidadeSaude);
    }

    /**
     * Get all the unidadeSaudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnidadeSaudeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UnidadeSaudes");
        return unidadeSaudeRepository.findAll(pageable)
            .map(unidadeSaudeMapper::toDto);
    }


    /**
     * Get one unidadeSaude by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadeSaudeDTO> findOne(Long id) {
        log.debug("Request to get UnidadeSaude : {}", id);
        return unidadeSaudeRepository.findById(id)
            .map(unidadeSaudeMapper::toDto);
    }

    /**
     * Delete the unidadeSaude by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UnidadeSaude : {}", id);
        unidadeSaudeRepository.deleteById(id);
    }
}

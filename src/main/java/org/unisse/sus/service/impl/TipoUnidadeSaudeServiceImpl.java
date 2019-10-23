package org.unisse.sus.service.impl;

import org.unisse.sus.service.TipoUnidadeSaudeService;
import org.unisse.sus.domain.TipoUnidadeSaude;
import org.unisse.sus.repository.TipoUnidadeSaudeRepository;
import org.unisse.sus.service.dto.TipoUnidadeSaudeDTO;
import org.unisse.sus.service.mapper.TipoUnidadeSaudeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TipoUnidadeSaude}.
 */
@Service
@Transactional
public class TipoUnidadeSaudeServiceImpl implements TipoUnidadeSaudeService {

    private final Logger log = LoggerFactory.getLogger(TipoUnidadeSaudeServiceImpl.class);

    private final TipoUnidadeSaudeRepository tipoUnidadeSaudeRepository;

    private final TipoUnidadeSaudeMapper tipoUnidadeSaudeMapper;

    public TipoUnidadeSaudeServiceImpl(TipoUnidadeSaudeRepository tipoUnidadeSaudeRepository, TipoUnidadeSaudeMapper tipoUnidadeSaudeMapper) {
        this.tipoUnidadeSaudeRepository = tipoUnidadeSaudeRepository;
        this.tipoUnidadeSaudeMapper = tipoUnidadeSaudeMapper;
    }

    /**
     * Save a tipoUnidadeSaude.
     *
     * @param tipoUnidadeSaudeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TipoUnidadeSaudeDTO save(TipoUnidadeSaudeDTO tipoUnidadeSaudeDTO) {
        log.debug("Request to save TipoUnidadeSaude : {}", tipoUnidadeSaudeDTO);
        TipoUnidadeSaude tipoUnidadeSaude = tipoUnidadeSaudeMapper.toEntity(tipoUnidadeSaudeDTO);
        tipoUnidadeSaude = tipoUnidadeSaudeRepository.save(tipoUnidadeSaude);
        return tipoUnidadeSaudeMapper.toDto(tipoUnidadeSaude);
    }

    /**
     * Get all the tipoUnidadeSaudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TipoUnidadeSaudeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoUnidadeSaudes");
        return tipoUnidadeSaudeRepository.findAll(pageable)
            .map(tipoUnidadeSaudeMapper::toDto);
    }


    /**
     * Get one tipoUnidadeSaude by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TipoUnidadeSaudeDTO> findOne(Long id) {
        log.debug("Request to get TipoUnidadeSaude : {}", id);
        return tipoUnidadeSaudeRepository.findById(id)
            .map(tipoUnidadeSaudeMapper::toDto);
    }

    /**
     * Delete the tipoUnidadeSaude by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoUnidadeSaude : {}", id);
        tipoUnidadeSaudeRepository.deleteById(id);
    }
}

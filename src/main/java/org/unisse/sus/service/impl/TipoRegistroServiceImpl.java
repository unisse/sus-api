package org.unisse.sus.service.impl;

import org.unisse.sus.service.TipoRegistroService;
import org.unisse.sus.domain.TipoRegistro;
import org.unisse.sus.repository.TipoRegistroRepository;
import org.unisse.sus.service.dto.TipoRegistroDTO;
import org.unisse.sus.service.mapper.TipoRegistroMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TipoRegistro}.
 */
@Service
@Transactional
public class TipoRegistroServiceImpl implements TipoRegistroService {

    private final Logger log = LoggerFactory.getLogger(TipoRegistroServiceImpl.class);

    private final TipoRegistroRepository tipoRegistroRepository;

    private final TipoRegistroMapper tipoRegistroMapper;

    public TipoRegistroServiceImpl(TipoRegistroRepository tipoRegistroRepository, TipoRegistroMapper tipoRegistroMapper) {
        this.tipoRegistroRepository = tipoRegistroRepository;
        this.tipoRegistroMapper = tipoRegistroMapper;
    }

    /**
     * Save a tipoRegistro.
     *
     * @param tipoRegistroDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TipoRegistroDTO save(TipoRegistroDTO tipoRegistroDTO) {
        log.debug("Request to save TipoRegistro : {}", tipoRegistroDTO);
        TipoRegistro tipoRegistro = tipoRegistroMapper.toEntity(tipoRegistroDTO);
        tipoRegistro = tipoRegistroRepository.save(tipoRegistro);
        return tipoRegistroMapper.toDto(tipoRegistro);
    }

    /**
     * Get all the tipoRegistros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TipoRegistroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoRegistros");
        return tipoRegistroRepository.findAll(pageable)
            .map(tipoRegistroMapper::toDto);
    }


    /**
     * Get one tipoRegistro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TipoRegistroDTO> findOne(Long id) {
        log.debug("Request to get TipoRegistro : {}", id);
        return tipoRegistroRepository.findById(id)
            .map(tipoRegistroMapper::toDto);
    }

    /**
     * Delete the tipoRegistro by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoRegistro : {}", id);
        tipoRegistroRepository.deleteById(id);
    }
}

package org.unisse.sus.service.impl;

import org.unisse.sus.service.ImportanciaService;
import org.unisse.sus.domain.Importancia;
import org.unisse.sus.repository.ImportanciaRepository;
import org.unisse.sus.service.dto.ImportanciaDTO;
import org.unisse.sus.service.mapper.ImportanciaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Importancia}.
 */
@Service
@Transactional
public class ImportanciaServiceImpl implements ImportanciaService {

    private final Logger log = LoggerFactory.getLogger(ImportanciaServiceImpl.class);

    private final ImportanciaRepository importanciaRepository;

    private final ImportanciaMapper importanciaMapper;

    public ImportanciaServiceImpl(ImportanciaRepository importanciaRepository, ImportanciaMapper importanciaMapper) {
        this.importanciaRepository = importanciaRepository;
        this.importanciaMapper = importanciaMapper;
    }

    /**
     * Save a importancia.
     *
     * @param importanciaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ImportanciaDTO save(ImportanciaDTO importanciaDTO) {
        log.debug("Request to save Importancia : {}", importanciaDTO);
        Importancia importancia = importanciaMapper.toEntity(importanciaDTO);
        importancia = importanciaRepository.save(importancia);
        return importanciaMapper.toDto(importancia);
    }

    /**
     * Get all the importancias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ImportanciaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Importancias");
        return importanciaRepository.findAll(pageable)
            .map(importanciaMapper::toDto);
    }


    /**
     * Get one importancia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ImportanciaDTO> findOne(Long id) {
        log.debug("Request to get Importancia : {}", id);
        return importanciaRepository.findById(id)
            .map(importanciaMapper::toDto);
    }

    /**
     * Delete the importancia by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Importancia : {}", id);
        importanciaRepository.deleteById(id);
    }
}

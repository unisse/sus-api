package org.unisse.sus.service.impl;

import org.unisse.sus.service.TipoDenunciaService;
import org.unisse.sus.domain.TipoDenuncia;
import org.unisse.sus.repository.TipoDenunciaRepository;
import org.unisse.sus.service.dto.TipoDenunciaDTO;
import org.unisse.sus.service.mapper.TipoDenunciaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TipoDenuncia}.
 */
@Service
@Transactional
public class TipoDenunciaServiceImpl implements TipoDenunciaService {

    private final Logger log = LoggerFactory.getLogger(TipoDenunciaServiceImpl.class);

    private final TipoDenunciaRepository tipoDenunciaRepository;

    private final TipoDenunciaMapper tipoDenunciaMapper;

    public TipoDenunciaServiceImpl(TipoDenunciaRepository tipoDenunciaRepository, TipoDenunciaMapper tipoDenunciaMapper) {
        this.tipoDenunciaRepository = tipoDenunciaRepository;
        this.tipoDenunciaMapper = tipoDenunciaMapper;
    }

    /**
     * Save a tipoDenuncia.
     *
     * @param tipoDenunciaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TipoDenunciaDTO save(TipoDenunciaDTO tipoDenunciaDTO) {
        log.debug("Request to save TipoDenuncia : {}", tipoDenunciaDTO);
        TipoDenuncia tipoDenuncia = tipoDenunciaMapper.toEntity(tipoDenunciaDTO);
        tipoDenuncia = tipoDenunciaRepository.save(tipoDenuncia);
        return tipoDenunciaMapper.toDto(tipoDenuncia);
    }

    /**
     * Get all the tipoDenuncias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TipoDenunciaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoDenuncias");
        return tipoDenunciaRepository.findAll(pageable)
            .map(tipoDenunciaMapper::toDto);
    }


    /**
     * Get one tipoDenuncia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TipoDenunciaDTO> findOne(Long id) {
        log.debug("Request to get TipoDenuncia : {}", id);
        return tipoDenunciaRepository.findById(id)
            .map(tipoDenunciaMapper::toDto);
    }

    /**
     * Delete the tipoDenuncia by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoDenuncia : {}", id);
        tipoDenunciaRepository.deleteById(id);
    }
}

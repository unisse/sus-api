package org.unisse.sus.service.impl;

import org.unisse.sus.service.DenunciaService;
import org.unisse.sus.domain.Denuncia;
import org.unisse.sus.repository.DenunciaRepository;
import org.unisse.sus.service.dto.DenunciaDTO;
import org.unisse.sus.service.mapper.DenunciaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Denuncia}.
 */
@Service
@Transactional
public class DenunciaServiceImpl implements DenunciaService {

    private final Logger log = LoggerFactory.getLogger(DenunciaServiceImpl.class);

    private final DenunciaRepository denunciaRepository;

    private final DenunciaMapper denunciaMapper;

    public DenunciaServiceImpl(DenunciaRepository denunciaRepository, DenunciaMapper denunciaMapper) {
        this.denunciaRepository = denunciaRepository;
        this.denunciaMapper = denunciaMapper;
    }

    /**
     * Save a denuncia.
     *
     * @param denunciaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DenunciaDTO save(DenunciaDTO denunciaDTO) {
        log.debug("Request to save Denuncia : {}", denunciaDTO);
        Denuncia denuncia = denunciaMapper.toEntity(denunciaDTO);
        denuncia = denunciaRepository.save(denuncia);
        return denunciaMapper.toDto(denuncia);
    }

    /**
     * Get all the denuncias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DenunciaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Denuncias");
        return denunciaRepository.findAll(pageable)
            .map(denunciaMapper::toDto);
    }


    /**
     * Get one denuncia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DenunciaDTO> findOne(Long id) {
        log.debug("Request to get Denuncia : {}", id);
        return denunciaRepository.findById(id)
            .map(denunciaMapper::toDto);
    }

    /**
     * Delete the denuncia by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Denuncia : {}", id);
        denunciaRepository.deleteById(id);
    }
}

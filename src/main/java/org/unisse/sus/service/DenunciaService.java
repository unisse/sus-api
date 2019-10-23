package org.unisse.sus.service;

import org.unisse.sus.service.dto.DenunciaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.Denuncia}.
 */
public interface DenunciaService {

    /**
     * Save a denuncia.
     *
     * @param denunciaDTO the entity to save.
     * @return the persisted entity.
     */
    DenunciaDTO save(DenunciaDTO denunciaDTO);

    /**
     * Get all the denuncias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DenunciaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" denuncia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DenunciaDTO> findOne(Long id);

    /**
     * Delete the "id" denuncia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

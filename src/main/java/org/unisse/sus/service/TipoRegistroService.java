package org.unisse.sus.service;

import org.unisse.sus.service.dto.TipoRegistroDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.TipoRegistro}.
 */
public interface TipoRegistroService {

    /**
     * Save a tipoRegistro.
     *
     * @param tipoRegistroDTO the entity to save.
     * @return the persisted entity.
     */
    TipoRegistroDTO save(TipoRegistroDTO tipoRegistroDTO);

    /**
     * Get all the tipoRegistros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoRegistroDTO> findAll(Pageable pageable);


    /**
     * Get the "id" tipoRegistro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoRegistroDTO> findOne(Long id);

    /**
     * Delete the "id" tipoRegistro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

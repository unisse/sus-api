package org.unisse.sus.service;

import org.unisse.sus.service.dto.ImportanciaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.Importancia}.
 */
public interface ImportanciaService {

    /**
     * Save a importancia.
     *
     * @param importanciaDTO the entity to save.
     * @return the persisted entity.
     */
    ImportanciaDTO save(ImportanciaDTO importanciaDTO);

    /**
     * Get all the importancias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImportanciaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" importancia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImportanciaDTO> findOne(Long id);

    /**
     * Delete the "id" importancia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

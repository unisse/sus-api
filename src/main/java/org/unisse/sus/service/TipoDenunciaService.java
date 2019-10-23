package org.unisse.sus.service;

import org.unisse.sus.service.dto.TipoDenunciaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.unisse.sus.domain.TipoDenuncia}.
 */
public interface TipoDenunciaService {

    /**
     * Save a tipoDenuncia.
     *
     * @param tipoDenunciaDTO the entity to save.
     * @return the persisted entity.
     */
    TipoDenunciaDTO save(TipoDenunciaDTO tipoDenunciaDTO);

    /**
     * Get all the tipoDenuncias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoDenunciaDTO> findAll(Pageable pageable);


    /**
     * Get the "id" tipoDenuncia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoDenunciaDTO> findOne(Long id);

    /**
     * Delete the "id" tipoDenuncia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package org.unisse.sus.service.impl;

import org.unisse.sus.service.ComentarioService;
import org.unisse.sus.domain.Comentario;
import org.unisse.sus.repository.ComentarioRepository;
import org.unisse.sus.service.dto.ComentarioDTO;
import org.unisse.sus.service.mapper.ComentarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Comentario}.
 */
@Service
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    private final Logger log = LoggerFactory.getLogger(ComentarioServiceImpl.class);

    private final ComentarioRepository comentarioRepository;

    private final ComentarioMapper comentarioMapper;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, ComentarioMapper comentarioMapper) {
        this.comentarioRepository = comentarioRepository;
        this.comentarioMapper = comentarioMapper;
    }

    /**
     * Save a comentario.
     *
     * @param comentarioDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ComentarioDTO save(ComentarioDTO comentarioDTO) {
        log.debug("Request to save Comentario : {}", comentarioDTO);
        Comentario comentario = comentarioMapper.toEntity(comentarioDTO);
        comentario = comentarioRepository.save(comentario);
        return comentarioMapper.toDto(comentario);
    }

    /**
     * Get all the comentarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ComentarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comentarios");
        return comentarioRepository.findAll(pageable)
            .map(comentarioMapper::toDto);
    }


    /**
     * Get one comentario by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ComentarioDTO> findOne(Long id) {
        log.debug("Request to get Comentario : {}", id);
        return comentarioRepository.findById(id)
            .map(comentarioMapper::toDto);
    }

    /**
     * Delete the comentario by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comentario : {}", id);
        comentarioRepository.deleteById(id);
    }
}

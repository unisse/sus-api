package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.ComentarioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comentario} and its DTO {@link ComentarioDTO}.
 */
@Mapper(componentModel = "spring", uses = {RegistroMapper.class})
public interface ComentarioMapper extends EntityMapper<ComentarioDTO, Comentario> {

    @Mapping(source = "registro.id", target = "registroId")
    ComentarioDTO toDto(Comentario comentario);

    @Mapping(source = "registroId", target = "registro")
    Comentario toEntity(ComentarioDTO comentarioDTO);

    default Comentario fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comentario comentario = new Comentario();
        comentario.setId(id);
        return comentario;
    }
}

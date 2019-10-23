package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.OpiniaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Opiniao} and its DTO {@link OpiniaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ComentarioMapper.class})
public interface OpiniaoMapper extends EntityMapper<OpiniaoDTO, Opiniao> {

    @Mapping(source = "comentario.id", target = "comentarioId")
    OpiniaoDTO toDto(Opiniao opiniao);

    @Mapping(source = "comentarioId", target = "comentario")
    Opiniao toEntity(OpiniaoDTO opiniaoDTO);

    default Opiniao fromId(Long id) {
        if (id == null) {
            return null;
        }
        Opiniao opiniao = new Opiniao();
        opiniao.setId(id);
        return opiniao;
    }
}

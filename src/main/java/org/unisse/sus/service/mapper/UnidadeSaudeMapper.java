package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.UnidadeSaudeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UnidadeSaude} and its DTO {@link UnidadeSaudeDTO}.
 */
@Mapper(componentModel = "spring", uses = {TipoUnidadeSaudeMapper.class, SituacaoUnidadeSaudeMapper.class})
public interface UnidadeSaudeMapper extends EntityMapper<UnidadeSaudeDTO, UnidadeSaude> {

    @Mapping(source = "tipo.id", target = "tipoId")
    @Mapping(source = "situacao.id", target = "situacaoId")
    UnidadeSaudeDTO toDto(UnidadeSaude unidadeSaude);

    @Mapping(source = "tipoId", target = "tipo")
    @Mapping(source = "situacaoId", target = "situacao")
    UnidadeSaude toEntity(UnidadeSaudeDTO unidadeSaudeDTO);

    default UnidadeSaude fromId(Long id) {
        if (id == null) {
            return null;
        }
        UnidadeSaude unidadeSaude = new UnidadeSaude();
        unidadeSaude.setId(id);
        return unidadeSaude;
    }
}

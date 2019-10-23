package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.SituacaoUnidadeSaudeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SituacaoUnidadeSaude} and its DTO {@link SituacaoUnidadeSaudeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SituacaoUnidadeSaudeMapper extends EntityMapper<SituacaoUnidadeSaudeDTO, SituacaoUnidadeSaude> {



    default SituacaoUnidadeSaude fromId(Long id) {
        if (id == null) {
            return null;
        }
        SituacaoUnidadeSaude situacaoUnidadeSaude = new SituacaoUnidadeSaude();
        situacaoUnidadeSaude.setId(id);
        return situacaoUnidadeSaude;
    }
}

package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.TipoUnidadeSaudeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoUnidadeSaude} and its DTO {@link TipoUnidadeSaudeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoUnidadeSaudeMapper extends EntityMapper<TipoUnidadeSaudeDTO, TipoUnidadeSaude> {



    default TipoUnidadeSaude fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoUnidadeSaude tipoUnidadeSaude = new TipoUnidadeSaude();
        tipoUnidadeSaude.setId(id);
        return tipoUnidadeSaude;
    }
}

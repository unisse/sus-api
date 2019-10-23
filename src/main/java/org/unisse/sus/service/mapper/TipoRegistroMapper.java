package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.TipoRegistroDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoRegistro} and its DTO {@link TipoRegistroDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoRegistroMapper extends EntityMapper<TipoRegistroDTO, TipoRegistro> {



    default TipoRegistro fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoRegistro tipoRegistro = new TipoRegistro();
        tipoRegistro.setId(id);
        return tipoRegistro;
    }
}

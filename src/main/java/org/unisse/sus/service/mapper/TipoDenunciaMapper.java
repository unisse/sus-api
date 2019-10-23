package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.TipoDenunciaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoDenuncia} and its DTO {@link TipoDenunciaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoDenunciaMapper extends EntityMapper<TipoDenunciaDTO, TipoDenuncia> {



    default TipoDenuncia fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoDenuncia tipoDenuncia = new TipoDenuncia();
        tipoDenuncia.setId(id);
        return tipoDenuncia;
    }
}

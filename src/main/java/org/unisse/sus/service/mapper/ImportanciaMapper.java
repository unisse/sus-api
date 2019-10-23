package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.ImportanciaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Importancia} and its DTO {@link ImportanciaDTO}.
 */
@Mapper(componentModel = "spring", uses = {RegistroMapper.class})
public interface ImportanciaMapper extends EntityMapper<ImportanciaDTO, Importancia> {

    @Mapping(source = "registro.id", target = "registroId")
    ImportanciaDTO toDto(Importancia importancia);

    @Mapping(source = "registroId", target = "registro")
    Importancia toEntity(ImportanciaDTO importanciaDTO);

    default Importancia fromId(Long id) {
        if (id == null) {
            return null;
        }
        Importancia importancia = new Importancia();
        importancia.setId(id);
        return importancia;
    }
}

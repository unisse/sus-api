package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.DenunciaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Denuncia} and its DTO {@link DenunciaDTO}.
 */
@Mapper(componentModel = "spring", uses = {RegistroMapper.class, TipoDenunciaMapper.class})
public interface DenunciaMapper extends EntityMapper<DenunciaDTO, Denuncia> {

    @Mapping(source = "registro.id", target = "registroId")
    @Mapping(source = "tipo.id", target = "tipoId")
    DenunciaDTO toDto(Denuncia denuncia);

    @Mapping(source = "registroId", target = "registro")
    @Mapping(source = "tipoId", target = "tipo")
    Denuncia toEntity(DenunciaDTO denunciaDTO);

    default Denuncia fromId(Long id) {
        if (id == null) {
            return null;
        }
        Denuncia denuncia = new Denuncia();
        denuncia.setId(id);
        return denuncia;
    }
}

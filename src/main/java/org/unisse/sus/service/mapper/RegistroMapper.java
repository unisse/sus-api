package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.RegistroDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Registro} and its DTO {@link RegistroDTO}.
 */
@Mapper(componentModel = "spring", uses = {TipoRegistroMapper.class, UnidadeSaudeMapper.class})
public interface RegistroMapper extends EntityMapper<RegistroDTO, Registro> {

    @Mapping(source = "tipo.id", target = "tipoId")
    @Mapping(source = "unidade.id", target = "unidadeId")
    RegistroDTO toDto(Registro registro);

    @Mapping(source = "tipoId", target = "tipo")
    @Mapping(source = "unidadeId", target = "unidade")
    Registro toEntity(RegistroDTO registroDTO);

    default Registro fromId(Long id) {
        if (id == null) {
            return null;
        }
        Registro registro = new Registro();
        registro.setId(id);
        return registro;
    }
}

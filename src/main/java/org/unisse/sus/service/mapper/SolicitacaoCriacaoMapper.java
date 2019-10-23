package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.SolicitacaoCriacaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SolicitacaoCriacao} and its DTO {@link SolicitacaoCriacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {UnidadeSaudeMapper.class})
public interface SolicitacaoCriacaoMapper extends EntityMapper<SolicitacaoCriacaoDTO, SolicitacaoCriacao> {

    @Mapping(source = "unidade.id", target = "unidadeId")
    SolicitacaoCriacaoDTO toDto(SolicitacaoCriacao solicitacaoCriacao);

    @Mapping(source = "unidadeId", target = "unidade")
    SolicitacaoCriacao toEntity(SolicitacaoCriacaoDTO solicitacaoCriacaoDTO);

    default SolicitacaoCriacao fromId(Long id) {
        if (id == null) {
            return null;
        }
        SolicitacaoCriacao solicitacaoCriacao = new SolicitacaoCriacao();
        solicitacaoCriacao.setId(id);
        return solicitacaoCriacao;
    }
}

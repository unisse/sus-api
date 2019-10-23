package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.SolicitacaoAtualizacaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SolicitacaoAtualizacao} and its DTO {@link SolicitacaoAtualizacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {UnidadeSaudeMapper.class})
public interface SolicitacaoAtualizacaoMapper extends EntityMapper<SolicitacaoAtualizacaoDTO, SolicitacaoAtualizacao> {

    @Mapping(source = "unidade.id", target = "unidadeId")
    SolicitacaoAtualizacaoDTO toDto(SolicitacaoAtualizacao solicitacaoAtualizacao);

    @Mapping(source = "unidadeId", target = "unidade")
    SolicitacaoAtualizacao toEntity(SolicitacaoAtualizacaoDTO solicitacaoAtualizacaoDTO);

    default SolicitacaoAtualizacao fromId(Long id) {
        if (id == null) {
            return null;
        }
        SolicitacaoAtualizacao solicitacaoAtualizacao = new SolicitacaoAtualizacao();
        solicitacaoAtualizacao.setId(id);
        return solicitacaoAtualizacao;
    }
}

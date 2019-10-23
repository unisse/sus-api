package org.unisse.sus.service.mapper;

import org.unisse.sus.domain.*;
import org.unisse.sus.service.dto.AlteracaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alteracao} and its DTO {@link AlteracaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {SolicitacaoAtualizacaoMapper.class})
public interface AlteracaoMapper extends EntityMapper<AlteracaoDTO, Alteracao> {

    @Mapping(source = "solicitacao.id", target = "solicitacaoId")
    AlteracaoDTO toDto(Alteracao alteracao);

    @Mapping(source = "solicitacaoId", target = "solicitacao")
    Alteracao toEntity(AlteracaoDTO alteracaoDTO);

    default Alteracao fromId(Long id) {
        if (id == null) {
            return null;
        }
        Alteracao alteracao = new Alteracao();
        alteracao.setId(id);
        return alteracao;
    }
}

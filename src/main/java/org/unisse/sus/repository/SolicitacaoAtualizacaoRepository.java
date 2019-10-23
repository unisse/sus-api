package org.unisse.sus.repository;
import org.unisse.sus.domain.SolicitacaoAtualizacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SolicitacaoAtualizacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolicitacaoAtualizacaoRepository extends JpaRepository<SolicitacaoAtualizacao, Long> {

}

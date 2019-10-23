package org.unisse.sus.repository;
import org.unisse.sus.domain.SolicitacaoCriacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SolicitacaoCriacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolicitacaoCriacaoRepository extends JpaRepository<SolicitacaoCriacao, Long> {

}

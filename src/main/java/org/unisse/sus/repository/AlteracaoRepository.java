package org.unisse.sus.repository;
import org.unisse.sus.domain.Alteracao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Alteracao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlteracaoRepository extends JpaRepository<Alteracao, Long> {

}

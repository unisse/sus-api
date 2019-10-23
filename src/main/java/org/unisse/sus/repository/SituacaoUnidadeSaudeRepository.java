package org.unisse.sus.repository;
import org.unisse.sus.domain.SituacaoUnidadeSaude;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SituacaoUnidadeSaude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituacaoUnidadeSaudeRepository extends JpaRepository<SituacaoUnidadeSaude, Long> {

}

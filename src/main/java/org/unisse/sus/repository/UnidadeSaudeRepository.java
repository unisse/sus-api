package org.unisse.sus.repository;
import org.unisse.sus.domain.UnidadeSaude;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UnidadeSaude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {

}

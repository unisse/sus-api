package org.unisse.sus.repository;
import org.unisse.sus.domain.TipoUnidadeSaude;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoUnidadeSaude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoUnidadeSaudeRepository extends JpaRepository<TipoUnidadeSaude, Long> {

}

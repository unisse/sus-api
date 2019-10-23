package org.unisse.sus.repository;
import org.unisse.sus.domain.TipoRegistro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoRegistro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoRegistroRepository extends JpaRepository<TipoRegistro, Long> {

}

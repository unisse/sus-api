package org.unisse.sus.repository;
import org.unisse.sus.domain.TipoDenuncia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoDenuncia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoDenunciaRepository extends JpaRepository<TipoDenuncia, Long> {

}

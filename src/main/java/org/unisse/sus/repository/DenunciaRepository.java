package org.unisse.sus.repository;
import org.unisse.sus.domain.Denuncia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Denuncia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

}

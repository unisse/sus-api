package org.unisse.sus.repository;
import org.unisse.sus.domain.Importancia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Importancia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImportanciaRepository extends JpaRepository<Importancia, Long> {

}

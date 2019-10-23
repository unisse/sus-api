package org.unisse.sus.repository;
import org.unisse.sus.domain.Registro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Registro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

}

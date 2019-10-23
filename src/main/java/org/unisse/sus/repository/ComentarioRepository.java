package org.unisse.sus.repository;
import org.unisse.sus.domain.Comentario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Comentario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}

package org.unisse.sus.repository;
import org.unisse.sus.domain.Opiniao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Opiniao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpiniaoRepository extends JpaRepository<Opiniao, Long> {

}

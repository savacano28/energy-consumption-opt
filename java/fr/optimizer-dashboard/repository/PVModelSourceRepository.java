package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.PVModelSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the PVModelSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PVModelSourceRepository extends JpaRepository<PVModelSource, Long> {

}

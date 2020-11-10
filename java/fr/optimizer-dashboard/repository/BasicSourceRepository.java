package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.BasicSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the BasicSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasicSourceRepository extends JpaRepository<BasicSource, Long> {

}

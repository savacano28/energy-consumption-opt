package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.CSVSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the CSVSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CSVSourceRepository extends JpaRepository<CSVSource, Long> {

}

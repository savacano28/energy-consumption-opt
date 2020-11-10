package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.HistorianSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Historian entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistorianRepository extends JpaRepository<HistorianSource, Long> {

}

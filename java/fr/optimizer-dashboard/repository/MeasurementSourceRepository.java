package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.MeasurementSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the MeasurementSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasurementSourceRepository extends JpaRepository<MeasurementSource, Long> {

}

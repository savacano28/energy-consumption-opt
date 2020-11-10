package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.BatteryModelSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the BatteryModelSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryModelSourceRepository extends JpaRepository<BatteryModelSource, Long> {

}

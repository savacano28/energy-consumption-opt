package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.BatteryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the BatteryManager entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryManagerRepository extends JpaRepository<BatteryManager, Long> {

}

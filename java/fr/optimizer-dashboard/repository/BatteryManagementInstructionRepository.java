package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.BatteryManagementInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the BatteryManagementInstruction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryManagementInstructionRepository extends JpaRepository<BatteryManagementInstruction, Long> {

}

package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.EnergyManagementSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the EnergyManagementSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnergyManagementSystemRepository extends JpaRepository<EnergyManagementSystem, Long> {

}

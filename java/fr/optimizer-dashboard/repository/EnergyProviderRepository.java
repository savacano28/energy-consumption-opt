package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.EnergyProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the EnergyProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnergyProviderRepository extends JpaRepository<EnergyProvider, Long> {

}

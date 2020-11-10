package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.FluxNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the EnergyElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FluxNodeRepository extends JpaRepository<FluxNode, Long> {

    @Query("select distinct fn from FluxNode fn where fn.type <> 'GROUP' ")
    List<FluxNode> findEnergyElements();

    @Query("select distinct fn from FluxNode fn where fn.type = 'GROUP' ")
    List<FluxNode> findFluxGroups();

}

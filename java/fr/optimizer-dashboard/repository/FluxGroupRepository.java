package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.FluxGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the FluxGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FluxGroupRepository extends JpaRepository<FluxGroup, Long> {

   /* @Query("select distinct fg from FluxGroup fg left join fetch fg.energyElements where fg.id = ?1 ")
    FluxGroup findByIdWithElements(Long fluxGroupId);

    @Query("select distinct fg from FluxGroup fg where fg.fluxTopology = ?1 ")
    Set<FluxGroup> findByIdFluxTopology(Long topologyId);*/
}

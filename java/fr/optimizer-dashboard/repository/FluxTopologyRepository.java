package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.FluxTopology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the FluxTopology entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FluxTopologyRepository extends JpaRepository<FluxTopology, Long> {

    /*@Query("select distinct ft from FluxTopology ft left join fetch ft.fluxGroups where ft.id = ?1 ")
    FluxTopology findByIdWithGroups(Long fluxTopologyId);*/
}

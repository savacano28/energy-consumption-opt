package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.EnergySite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the EnergySite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnergySiteRepository extends JpaRepository<EnergySite, Long> {

 /*   @Query("select fluxTopology from FluxTopology flux_topology where flux_topology.energySite.id =:id ")
    Set<FluxTopology> findAllTopologies(@Param("id") Long id);*/

   /* @Query("select distinct es from EnergySite es left join fetch es.fluxTopologies where es.id = ?1 ")
    EnergySite findByIdWithTopologies(Long energySiteId);*/

}

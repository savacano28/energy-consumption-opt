package fr.ifpen.synergreen.repository;


import fr.ifpen.synergreen.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /*   @Query("select fluxTopology from FluxTopology flux_topology where flux_topology.energySite.id =:id ")
    Set<FluxTopology> findAllTopologies(@Param("id") Long id);*/

   /* @Query("select distinct inv from Invoice inv where inv.flux_topology_id = ?1 ")
    List<Invoice> findByI(Long energySiteId);
*/
}

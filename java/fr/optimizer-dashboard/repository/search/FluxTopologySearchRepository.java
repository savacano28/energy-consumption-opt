package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.FluxTopology;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FluxTopology entity.
 */
public interface FluxTopologySearchRepository extends ElasticsearchRepository<FluxTopology, Long> {
}

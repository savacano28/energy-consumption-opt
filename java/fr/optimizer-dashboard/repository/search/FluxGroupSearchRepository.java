package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.FluxGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FluxGroup entity.
 */
public interface FluxGroupSearchRepository extends ElasticsearchRepository<FluxGroup, Long> {
}

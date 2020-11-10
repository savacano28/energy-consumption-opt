package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.EnergyElement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EnergyElement entity.
 */
public interface EnergyElementSearchRepository extends ElasticsearchRepository<EnergyElement, Long> {
}

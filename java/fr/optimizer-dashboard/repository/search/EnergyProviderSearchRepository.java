package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.EnergyProvider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EnergyProvider entity.
 */
public interface EnergyProviderSearchRepository extends ElasticsearchRepository<EnergyProvider, Long> {
}

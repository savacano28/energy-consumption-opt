package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.EnergyManagementSystem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EnergyManagementSystem entity.
 */
public interface EnergyManagementSystemSearchRepository extends ElasticsearchRepository<EnergyManagementSystem, Long> {
}

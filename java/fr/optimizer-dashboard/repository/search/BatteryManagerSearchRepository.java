package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.BatteryManager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BatteryManager entity.
 */
public interface BatteryManagerSearchRepository extends ElasticsearchRepository<BatteryManager, Long> {
}

package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.BatteryModelSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BatteryModelSource entity.
 */
public interface BatteryModelSourceSearchRepository extends ElasticsearchRepository<BatteryModelSource, Long> {
}

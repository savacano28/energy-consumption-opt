package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.BatteryManagementRun;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BatteryManagementRun entity.
 */
public interface BatteryManagementRunSearchRepository extends ElasticsearchRepository<BatteryManagementRun, Long> {
}

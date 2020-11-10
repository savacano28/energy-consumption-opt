package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.BatteryManagementInstruction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BatteryManagementInstruction entity.
 */
public interface BatteryManagementInstructionSearchRepository extends ElasticsearchRepository<BatteryManagementInstruction, Long> {
}

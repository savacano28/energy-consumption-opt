package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.HistorianSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Historian entity.
 */
public interface HistorianSearchRepository extends ElasticsearchRepository<HistorianSource, Long> {
}

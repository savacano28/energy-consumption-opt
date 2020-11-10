package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.EnergySite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EnergySite entity.
 */
public interface EnergySiteSearchRepository extends ElasticsearchRepository<EnergySite, Long> {
}

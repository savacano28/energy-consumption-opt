package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.PVModelSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PVModelSource entity.
 */
public interface PVModelSourceSearchRepository extends ElasticsearchRepository<PVModelSource, Long> {
}

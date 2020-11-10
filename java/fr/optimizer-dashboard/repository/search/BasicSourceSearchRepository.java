package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.BasicSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BasicSource entity.
 */
public interface BasicSourceSearchRepository extends ElasticsearchRepository<BasicSource, Long> {
}

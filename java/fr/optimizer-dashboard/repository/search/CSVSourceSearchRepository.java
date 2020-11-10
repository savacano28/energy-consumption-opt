package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.CSVSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CSVSource entity.
 */
public interface CSVSourceSearchRepository extends ElasticsearchRepository<CSVSource, Long> {
}

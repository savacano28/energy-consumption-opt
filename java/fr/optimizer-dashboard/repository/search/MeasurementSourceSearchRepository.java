package fr.ifpen.synergreen.repository.search;

import fr.ifpen.synergreen.domain.MeasurementSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MeasurementSource entity.
 */
public interface MeasurementSourceSearchRepository extends ElasticsearchRepository<MeasurementSource, Long> {
}

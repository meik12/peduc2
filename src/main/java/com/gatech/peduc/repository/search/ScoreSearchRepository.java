package com.gatech.peduc.repository.search;

import com.gatech.peduc.domain.Score;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Score entity.
 */
public interface ScoreSearchRepository extends ElasticsearchRepository<Score, Long> {
}

package com.gatech.peduc.repository.search;

import com.gatech.peduc.domain.ScoreUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ScoreUser entity.
 */
public interface ScoreUserSearchRepository extends ElasticsearchRepository<ScoreUser, Long> {
}

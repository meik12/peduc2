package com.gatech.peduc.repository.search;

import com.gatech.peduc.domain.SuggestedLecture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SuggestedLecture entity.
 */
public interface SuggestedLectureSearchRepository extends ElasticsearchRepository<SuggestedLecture, Long> {
}

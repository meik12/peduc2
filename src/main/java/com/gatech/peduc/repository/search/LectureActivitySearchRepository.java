package com.gatech.peduc.repository.search;

import com.gatech.peduc.domain.LectureActivity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LectureActivity entity.
 */
public interface LectureActivitySearchRepository extends ElasticsearchRepository<LectureActivity, Long> {
}

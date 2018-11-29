package com.gatech.peduc.repository.search;

import com.gatech.peduc.domain.Lecture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Lecture entity.
 */
public interface LectureSearchRepository extends ElasticsearchRepository<Lecture, Long> {
}

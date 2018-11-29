package com.gatech.peduc.repository.search;

import com.gatech.peduc.domain.Peer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Peer entity.
 */
public interface PeerSearchRepository extends ElasticsearchRepository<Peer, Long> {
}

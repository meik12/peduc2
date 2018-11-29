package com.gatech.peduc.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PeerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PeerSearchRepositoryMockConfiguration {

    @MockBean
    private PeerSearchRepository mockPeerSearchRepository;

}
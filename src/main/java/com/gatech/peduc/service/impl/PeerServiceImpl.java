package com.gatech.peduc.service.impl;

import com.gatech.peduc.service.PeerService;
import com.gatech.peduc.domain.Peer;
import com.gatech.peduc.repository.PeerRepository;
import com.gatech.peduc.repository.search.PeerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Peer.
 */
@Service
@Transactional
public class PeerServiceImpl implements PeerService {

    private final Logger log = LoggerFactory.getLogger(PeerServiceImpl.class);

    private PeerRepository peerRepository;

    private PeerSearchRepository peerSearchRepository;

    public PeerServiceImpl(PeerRepository peerRepository, PeerSearchRepository peerSearchRepository) {
        this.peerRepository = peerRepository;
        this.peerSearchRepository = peerSearchRepository;
    }

    /**
     * Save a peer.
     *
     * @param peer the entity to save
     * @return the persisted entity
     */
    @Override
    public Peer save(Peer peer) {
        log.debug("Request to save Peer : {}", peer);
        Peer result = peerRepository.save(peer);
        peerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the peers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Peer> findAll() {
        log.debug("Request to get all Peers");
        return peerRepository.findAll();
    }


    /**
     * Get one peer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Peer> findOne(Long id) {
        log.debug("Request to get Peer : {}", id);
        return peerRepository.findById(id);
    }

    /**
     * Delete the peer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Peer : {}", id);
        peerRepository.deleteById(id);
        peerSearchRepository.deleteById(id);
    }

    /**
     * Search for the peer corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Peer> search(String query) {
        log.debug("Request to search Peers for query {}", query);
        return StreamSupport
            .stream(peerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

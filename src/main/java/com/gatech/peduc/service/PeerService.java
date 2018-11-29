package com.gatech.peduc.service;

import com.gatech.peduc.domain.Peer;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Peer.
 */
public interface PeerService {

    /**
     * Save a peer.
     *
     * @param peer the entity to save
     * @return the persisted entity
     */
    Peer save(Peer peer);

    /**
     * Get all the peers.
     *
     * @return the list of entities
     */
    List<Peer> findAll();


    /**
     * Get the "id" peer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Peer> findOne(Long id);

    /**
     * Delete the "id" peer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the peer corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Peer> search(String query);
}

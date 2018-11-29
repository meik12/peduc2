package com.gatech.peduc.service;

import com.gatech.peduc.domain.Score;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Score.
 */
public interface ScoreService {

    /**
     * Save a score.
     *
     * @param score the entity to save
     * @return the persisted entity
     */
    Score save(Score score);

    /**
     * Get all the scores.
     *
     * @return the list of entities
     */
    List<Score> findAll();
    /**
     * Get all the ScoreDTO where Peer is null.
     *
     * @return the list of entities
     */
    List<Score> findAllWherePeerIsNull();


    /**
     * Get the "id" score.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Score> findOne(Long id);

    /**
     * Delete the "id" score.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the score corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Score> search(String query);
}

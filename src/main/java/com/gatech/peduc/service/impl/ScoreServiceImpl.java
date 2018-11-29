package com.gatech.peduc.service.impl;

import com.gatech.peduc.service.ScoreService;
import com.gatech.peduc.domain.Score;
import com.gatech.peduc.repository.ScoreRepository;
import com.gatech.peduc.repository.search.ScoreSearchRepository;
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
 * Service Implementation for managing Score.
 */
@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {

    private final Logger log = LoggerFactory.getLogger(ScoreServiceImpl.class);

    private ScoreRepository scoreRepository;

    private ScoreSearchRepository scoreSearchRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository, ScoreSearchRepository scoreSearchRepository) {
        this.scoreRepository = scoreRepository;
        this.scoreSearchRepository = scoreSearchRepository;
    }

    /**
     * Save a score.
     *
     * @param score the entity to save
     * @return the persisted entity
     */
    @Override
    public Score save(Score score) {
        log.debug("Request to save Score : {}", score);
        Score result = scoreRepository.save(score);
        scoreSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the scores.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Score> findAll() {
        log.debug("Request to get all Scores");
        return scoreRepository.findAll();
    }



    /**
     *  get all the scores where Peer is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Score> findAllWherePeerIsNull() {
        log.debug("Request to get all scores where Peer is null");
        return StreamSupport
            .stream(scoreRepository.findAll().spliterator(), false)
            .filter(score -> score.getPeer() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one score by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Score> findOne(Long id) {
        log.debug("Request to get Score : {}", id);
        return scoreRepository.findById(id);
    }

    /**
     * Delete the score by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Score : {}", id);
        scoreRepository.deleteById(id);
        scoreSearchRepository.deleteById(id);
    }

    /**
     * Search for the score corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Score> search(String query) {
        log.debug("Request to search Scores for query {}", query);
        return StreamSupport
            .stream(scoreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

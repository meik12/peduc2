package com.gatech.peduc.service.impl;

import com.gatech.peduc.service.SuggestedLectureService;
import com.gatech.peduc.service.UserService;
import com.gatech.peduc.domain.SuggestedLecture;
import com.gatech.peduc.repository.SuggestedLectureRepository;
import com.gatech.peduc.repository.search.SuggestedLectureSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SuggestedLecture.
 */
@Service
@Transactional
public class SuggestedLectureServiceImpl implements SuggestedLectureService {

    private final Logger log = LoggerFactory.getLogger(SuggestedLectureServiceImpl.class);

    private SuggestedLectureRepository suggestedLectureRepository;

    private SuggestedLectureSearchRepository suggestedLectureSearchRepository;

    private UserService userService;
    public SuggestedLectureServiceImpl(UserService userService, SuggestedLectureRepository suggestedLectureRepository, SuggestedLectureSearchRepository suggestedLectureSearchRepository) {
        this.suggestedLectureRepository = suggestedLectureRepository;
        this.suggestedLectureSearchRepository = suggestedLectureSearchRepository;
        this.userService = userService;
    }

    /**
     * Save a suggestedLecture.
     *
     * @param suggestedLecture the entity to save
     * @return the persisted entity
     */
    @Override
    public SuggestedLecture save(SuggestedLecture suggestedLecture) {
        log.debug("Request to save SuggestedLecture : {}", suggestedLecture);
        SuggestedLecture result = suggestedLectureRepository.save(suggestedLecture);
        suggestedLectureSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the suggestedLectures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SuggestedLecture> findAll(Pageable pageable) {
        
        Long id = userService.getUserWithAuthorities().get().getId();
        log.debug("Request to get all SuggestedLectures");
        return suggestedLectureRepository.findByUserIsCurrentUser(id, pageable);
    }


    /**
     * Get one suggestedLecture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SuggestedLecture> findOne(Long id) {
        log.debug("Request to get SuggestedLecture : {}", id);
        return suggestedLectureRepository.findById(id);
    }

    /**
     * Delete the suggestedLecture by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SuggestedLecture : {}", id);
        suggestedLectureRepository.deleteById(id);
        suggestedLectureSearchRepository.deleteById(id);
    }

    /**
     * Search for the suggestedLecture corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SuggestedLecture> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SuggestedLectures for query {}", query);
        return suggestedLectureSearchRepository.search(queryStringQuery(query), pageable);    }
}

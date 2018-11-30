package com.gatech.peduc.service.impl;

import com.gatech.peduc.service.LectureService;
import com.gatech.peduc.service.UserService;
import com.gatech.peduc.domain.Lecture;
import com.gatech.peduc.repository.LectureRepository;
import com.gatech.peduc.repository.search.LectureSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Lecture.
 */
@Service
@Transactional
public class LectureServiceImpl implements LectureService {

    private final Logger log = LoggerFactory.getLogger(LectureServiceImpl.class);

    private LectureRepository lectureRepository;

    private LectureSearchRepository lectureSearchRepository;
    private UserService userService;

    public LectureServiceImpl(UserService userService, LectureRepository lectureRepository, LectureSearchRepository lectureSearchRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureSearchRepository = lectureSearchRepository;
        this.userService = userService;
    }

    /**
     * Save a lecture.
     *
     * @param lecture the entity to save
     * @return the persisted entity
     */
    @Override
    public Lecture save(Lecture lecture) {
        log.debug("Request to save Lecture : {}", lecture);
        Lecture result = lectureRepository.save(lecture);
        lectureSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the lectures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Lecture> findAll(Pageable pageable) {
        log.debug("Request to get all Lectures");
        Long id = userService.getUserWithAuthorities().get().getId();
        return lectureRepository.findByUserIsCurrentUser(id,pageable);
    }


    /**
     * Get one lecture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Lecture> findOne(Long id) {
        log.debug("Request to get Lecture : {}", id);
        return lectureRepository.findById(id);
    }

    /**
     * Delete the lecture by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lecture : {}", id);
        lectureRepository.deleteById(id);
        lectureSearchRepository.deleteById(id);
    }

    /**
     * Search for the lecture corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Lecture> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lectures for query {}", query);
        return lectureSearchRepository.search(queryStringQuery(query), pageable);    }
}

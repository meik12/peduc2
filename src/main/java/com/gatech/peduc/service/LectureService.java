package com.gatech.peduc.service;

import com.gatech.peduc.domain.Lecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Lecture.
 */
public interface LectureService {

    /**
     * Save a lecture.
     *
     * @param lecture the entity to save
     * @return the persisted entity
     */
    Lecture save(Lecture lecture);

    /**
     * Get all the lectures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Lecture> findAllExceptCurrentUser(Pageable pageable);

    Page<Lecture> findAllForCurrent(Pageable pageable);


    /**
     * Get the "id" lecture.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Lecture> findOne(Long id);

    /**
     * Delete the "id" lecture.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lecture corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Lecture> search(String query, Pageable pageable);
}

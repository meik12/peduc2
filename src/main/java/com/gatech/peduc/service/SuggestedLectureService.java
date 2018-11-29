package com.gatech.peduc.service;

import com.gatech.peduc.domain.SuggestedLecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SuggestedLecture.
 */
public interface SuggestedLectureService {

    /**
     * Save a suggestedLecture.
     *
     * @param suggestedLecture the entity to save
     * @return the persisted entity
     */
    SuggestedLecture save(SuggestedLecture suggestedLecture);

    /**
     * Get all the suggestedLectures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SuggestedLecture> findAll(Pageable pageable);


    /**
     * Get the "id" suggestedLecture.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SuggestedLecture> findOne(Long id);

    /**
     * Delete the "id" suggestedLecture.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the suggestedLecture corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SuggestedLecture> search(String query, Pageable pageable);
}

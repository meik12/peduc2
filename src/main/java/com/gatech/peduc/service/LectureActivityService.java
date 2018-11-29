package com.gatech.peduc.service;

import com.gatech.peduc.domain.LectureActivity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing LectureActivity.
 */
public interface LectureActivityService {

    /**
     * Save a lectureActivity.
     *
     * @param lectureActivity the entity to save
     * @return the persisted entity
     */
    LectureActivity save(LectureActivity lectureActivity);

    /**
     * Get all the lectureActivities.
     *
     * @return the list of entities
     */
    List<LectureActivity> findAll();

    /**
     * Get all the LectureActivity with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<LectureActivity> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" lectureActivity.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LectureActivity> findOne(Long id);

    /**
     * Delete the "id" lectureActivity.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lectureActivity corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<LectureActivity> search(String query);
}

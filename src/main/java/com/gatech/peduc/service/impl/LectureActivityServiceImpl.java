package com.gatech.peduc.service.impl;

import com.gatech.peduc.service.LectureActivityService;
import com.gatech.peduc.domain.LectureActivity;
import com.gatech.peduc.repository.LectureActivityRepository;
import com.gatech.peduc.repository.search.LectureActivitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LectureActivity.
 */
@Service
@Transactional
public class LectureActivityServiceImpl implements LectureActivityService {

    private final Logger log = LoggerFactory.getLogger(LectureActivityServiceImpl.class);

    private LectureActivityRepository lectureActivityRepository;

    private LectureActivitySearchRepository lectureActivitySearchRepository;

    public LectureActivityServiceImpl(LectureActivityRepository lectureActivityRepository, LectureActivitySearchRepository lectureActivitySearchRepository) {
        this.lectureActivityRepository = lectureActivityRepository;
        this.lectureActivitySearchRepository = lectureActivitySearchRepository;
    }

    /**
     * Save a lectureActivity.
     *
     * @param lectureActivity the entity to save
     * @return the persisted entity
     */
    @Override
    public LectureActivity save(LectureActivity lectureActivity) {
        log.debug("Request to save LectureActivity : {}", lectureActivity);
        LectureActivity result = lectureActivityRepository.save(lectureActivity);
        lectureActivitySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the lectureActivities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LectureActivity> findAll() {
        log.debug("Request to get all LectureActivities");
        return lectureActivityRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the LectureActivity with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<LectureActivity> findAllWithEagerRelationships(Pageable pageable) {
        return lectureActivityRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one lectureActivity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LectureActivity> findOne(Long id) {
        log.debug("Request to get LectureActivity : {}", id);
        return lectureActivityRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the lectureActivity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LectureActivity : {}", id);
        lectureActivityRepository.deleteById(id);
        lectureActivitySearchRepository.deleteById(id);
    }

    /**
     * Search for the lectureActivity corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LectureActivity> search(String query) {
        log.debug("Request to search LectureActivities for query {}", query);
        return StreamSupport
            .stream(lectureActivitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

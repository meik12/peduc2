package com.gatech.peduc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gatech.peduc.domain.LectureActivity;
import com.gatech.peduc.service.LectureActivityService;
import com.gatech.peduc.web.rest.errors.BadRequestAlertException;
import com.gatech.peduc.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LectureActivity.
 */
@RestController
@RequestMapping("/api")
public class LectureActivityResource {

    private final Logger log = LoggerFactory.getLogger(LectureActivityResource.class);

    private static final String ENTITY_NAME = "lectureActivity";

    private LectureActivityService lectureActivityService;

    public LectureActivityResource(LectureActivityService lectureActivityService) {
        this.lectureActivityService = lectureActivityService;
    }

    /**
     * POST  /lecture-activities : Create a new lectureActivity.
     *
     * @param lectureActivity the lectureActivity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lectureActivity, or with status 400 (Bad Request) if the lectureActivity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lecture-activities")
    @Timed
    public ResponseEntity<LectureActivity> createLectureActivity(@RequestBody LectureActivity lectureActivity) throws URISyntaxException {
        log.debug("REST request to save LectureActivity : {}", lectureActivity);
        if (lectureActivity.getId() != null) {
            throw new BadRequestAlertException("A new lectureActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LectureActivity result = lectureActivityService.save(lectureActivity);
        return ResponseEntity.created(new URI("/api/lecture-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lecture-activities : Updates an existing lectureActivity.
     *
     * @param lectureActivity the lectureActivity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lectureActivity,
     * or with status 400 (Bad Request) if the lectureActivity is not valid,
     * or with status 500 (Internal Server Error) if the lectureActivity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lecture-activities")
    @Timed
    public ResponseEntity<LectureActivity> updateLectureActivity(@RequestBody LectureActivity lectureActivity) throws URISyntaxException {
        log.debug("REST request to update LectureActivity : {}", lectureActivity);
        if (lectureActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LectureActivity result = lectureActivityService.save(lectureActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lectureActivity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lecture-activities : get all the lectureActivities.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of lectureActivities in body
     */
    @GetMapping("/lecture-activities")
    @Timed
    public List<LectureActivity> getAllLectureActivities(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LectureActivities");
        return lectureActivityService.findAll();
    }

    /**
     * GET  /lecture-activities/:id : get the "id" lectureActivity.
     *
     * @param id the id of the lectureActivity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lectureActivity, or with status 404 (Not Found)
     */
    @GetMapping("/lecture-activities/{id}")
    @Timed
    public ResponseEntity<LectureActivity> getLectureActivity(@PathVariable Long id) {
        log.debug("REST request to get LectureActivity : {}", id);
        Optional<LectureActivity> lectureActivity = lectureActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lectureActivity);
    }

    /**
     * DELETE  /lecture-activities/:id : delete the "id" lectureActivity.
     *
     * @param id the id of the lectureActivity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lecture-activities/{id}")
    @Timed
    public ResponseEntity<Void> deleteLectureActivity(@PathVariable Long id) {
        log.debug("REST request to delete LectureActivity : {}", id);
        lectureActivityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lecture-activities?query=:query : search for the lectureActivity corresponding
     * to the query.
     *
     * @param query the query of the lectureActivity search
     * @return the result of the search
     */
    @GetMapping("/_search/lecture-activities")
    @Timed
    public List<LectureActivity> searchLectureActivities(@RequestParam String query) {
        log.debug("REST request to search LectureActivities for query {}", query);
        return lectureActivityService.search(query);
    }

}

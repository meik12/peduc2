package com.gatech.peduc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gatech.peduc.domain.SuggestedLecture;
import com.gatech.peduc.service.SuggestedLectureService;
import com.gatech.peduc.web.rest.errors.BadRequestAlertException;
import com.gatech.peduc.web.rest.util.HeaderUtil;
import com.gatech.peduc.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SuggestedLecture.
 */
@RestController
@RequestMapping("/api")
public class SuggestedLectureResource {

    private final Logger log = LoggerFactory.getLogger(SuggestedLectureResource.class);

    private static final String ENTITY_NAME = "suggestedLecture";

    private SuggestedLectureService suggestedLectureService;

    public SuggestedLectureResource(SuggestedLectureService suggestedLectureService) {
        this.suggestedLectureService = suggestedLectureService;
    }

    /**
     * POST  /suggested-lectures : Create a new suggestedLecture.
     *
     * @param suggestedLecture the suggestedLecture to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suggestedLecture, or with status 400 (Bad Request) if the suggestedLecture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suggested-lectures")
    @Timed
    public ResponseEntity<SuggestedLecture> createSuggestedLecture(@Valid @RequestBody SuggestedLecture suggestedLecture) throws URISyntaxException {
        log.debug("REST request to save SuggestedLecture : {}", suggestedLecture);
        if (suggestedLecture.getId() != null) {
            throw new BadRequestAlertException("A new suggestedLecture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuggestedLecture result = suggestedLectureService.save(suggestedLecture);
        return ResponseEntity.created(new URI("/api/suggested-lectures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suggested-lectures : Updates an existing suggestedLecture.
     *
     * @param suggestedLecture the suggestedLecture to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suggestedLecture,
     * or with status 400 (Bad Request) if the suggestedLecture is not valid,
     * or with status 500 (Internal Server Error) if the suggestedLecture couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suggested-lectures")
    @Timed
    public ResponseEntity<SuggestedLecture> updateSuggestedLecture(@Valid @RequestBody SuggestedLecture suggestedLecture) throws URISyntaxException {
        log.debug("REST request to update SuggestedLecture : {}", suggestedLecture);
        if (suggestedLecture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SuggestedLecture result = suggestedLectureService.save(suggestedLecture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suggestedLecture.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suggested-lectures : get all the suggestedLectures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of suggestedLectures in body
     */
    @GetMapping("/suggested-lectures")
    @Timed
    public ResponseEntity<List<SuggestedLecture>> getAllSuggestedLectures(Pageable pageable) {
        log.debug("REST request to get a page of SuggestedLectures");
        Page<SuggestedLecture> page = suggestedLectureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/suggested-lectures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /suggested-lectures/:id : get the "id" suggestedLecture.
     *
     * @param id the id of the suggestedLecture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suggestedLecture, or with status 404 (Not Found)
     */
    @GetMapping("/suggested-lectures/{id}")
    @Timed
    public ResponseEntity<SuggestedLecture> getSuggestedLecture(@PathVariable Long id) {
        log.debug("REST request to get SuggestedLecture : {}", id);
        Optional<SuggestedLecture> suggestedLecture = suggestedLectureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suggestedLecture);
    }

    /**
     * DELETE  /suggested-lectures/:id : delete the "id" suggestedLecture.
     *
     * @param id the id of the suggestedLecture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suggested-lectures/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuggestedLecture(@PathVariable Long id) {
        log.debug("REST request to delete SuggestedLecture : {}", id);
        suggestedLectureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/suggested-lectures?query=:query : search for the suggestedLecture corresponding
     * to the query.
     *
     * @param query the query of the suggestedLecture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/suggested-lectures")
    @Timed
    public ResponseEntity<List<SuggestedLecture>> searchSuggestedLectures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SuggestedLectures for query {}", query);
        Page<SuggestedLecture> page = suggestedLectureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/suggested-lectures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

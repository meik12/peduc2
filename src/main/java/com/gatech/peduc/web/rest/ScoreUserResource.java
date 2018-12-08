package com.gatech.peduc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gatech.peduc.domain.ScoreUser;
import com.gatech.peduc.domain.User;
import com.gatech.peduc.repository.ScoreUserRepository;
import com.gatech.peduc.repository.UserRepository;
import com.gatech.peduc.repository.search.ScoreUserSearchRepository;
import com.gatech.peduc.service.impl.LectureServiceImpl;
import com.gatech.peduc.web.rest.errors.BadRequestAlertException;
import com.gatech.peduc.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ScoreUser.
 */
@RestController
@RequestMapping("/api")
public class ScoreUserResource {

    private final Logger log = LoggerFactory.getLogger(ScoreUserResource.class);

    private static final String ENTITY_NAME = "scoreUser";

    private ScoreUserRepository scoreUserRepository;

    private ScoreUserSearchRepository scoreUserSearchRepository;
    private UserRepository userRepository;
    private LectureServiceImpl lectureServiceImpl;

    public ScoreUserResource(UserRepository userRepository, ScoreUserRepository scoreUserRepository, ScoreUserSearchRepository scoreUserSearchRepository) {
        this.scoreUserRepository = scoreUserRepository;
        this.scoreUserSearchRepository = scoreUserSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /score-users : Create a new scoreUser.
     *
     * @param scoreUser the scoreUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scoreUser, or with status 400 (Bad Request) if the scoreUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/score-users")
    @Timed
    public ResponseEntity<ScoreUser> createScoreUser(@Valid @RequestBody ScoreUser scoreUser) throws URISyntaxException {
        log.debug("REST request to save ScoreUser : {}", scoreUser);
        if (scoreUser.getId() != null) {
            throw new BadRequestAlertException("A new scoreUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScoreUser result = scoreUserRepository.save(scoreUser);
        scoreUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/score-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /score-users : Updates an existing scoreUser.
     *
     * @param scoreUser the scoreUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scoreUser,
     * or with status 400 (Bad Request) if the scoreUser is not valid,
     * or with status 500 (Internal Server Error) if the scoreUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/score-users")
    @Timed
    public ResponseEntity<ScoreUser> updateScoreUser(@Valid @RequestBody ScoreUser scoreUser) throws URISyntaxException {
        log.debug("REST request to update ScoreUser : {}", scoreUser);
        if (scoreUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScoreUser result = scoreUserRepository.save(scoreUser);
        scoreUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scoreUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /score-users : get all the scoreUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of scoreUsers in body
     */
    @GetMapping("/score-users")
    @Timed
    public List<ScoreUser> getAllScoreUsers() {
        log.debug("REST request to get all ScoreUsers");
        User user = new User();
        user=userRepository.findOneByLogin(lectureServiceImpl.getCurrentUserLogin()).get();
        return scoreUserRepository.findByUserIsNotCurrentUser(user.getId());
    }

    /**
     * GET  /score-users/:id : get the "id" scoreUser.
     *
     * @param id the id of the scoreUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scoreUser, or with status 404 (Not Found)
     */
    @GetMapping("/score-users/{id}")
    @Timed
    public ResponseEntity<ScoreUser> getScoreUser(@PathVariable Long id) {
        log.debug("REST request to get ScoreUser : {}", id);
        Optional<ScoreUser> scoreUser = scoreUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scoreUser);
    }

    /**
     * DELETE  /score-users/:id : delete the "id" scoreUser.
     *
     * @param id the id of the scoreUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/score-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteScoreUser(@PathVariable Long id) {
        log.debug("REST request to delete ScoreUser : {}", id);

        scoreUserRepository.deleteById(id);
        scoreUserSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/score-users?query=:query : search for the scoreUser corresponding
     * to the query.
     *
     * @param query the query of the scoreUser search
     * @return the result of the search
     */
    @GetMapping("/_search/score-users")
    @Timed
    public List<ScoreUser> searchScoreUsers(@RequestParam String query) {
        log.debug("REST request to search ScoreUsers for query {}", query);
        return StreamSupport
            .stream(scoreUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

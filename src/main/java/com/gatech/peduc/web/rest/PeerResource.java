package com.gatech.peduc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gatech.peduc.domain.Peer;
import com.gatech.peduc.service.PeerService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Peer.
 */
@RestController
@RequestMapping("/api")
public class PeerResource {

    private final Logger log = LoggerFactory.getLogger(PeerResource.class);

    private static final String ENTITY_NAME = "peer";

    private PeerService peerService;

    public PeerResource(PeerService peerService) {
        this.peerService = peerService;
    }

    /**
     * POST  /peers : Create a new peer.
     *
     * @param peer the peer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new peer, or with status 400 (Bad Request) if the peer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/peers")
    @Timed
    public ResponseEntity<Peer> createPeer(@Valid @RequestBody Peer peer) throws URISyntaxException {
        log.debug("REST request to save Peer : {}", peer);
        if (peer.getId() != null) {
            throw new BadRequestAlertException("A new peer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Peer result = peerService.save(peer);
        return ResponseEntity.created(new URI("/api/peers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /peers : Updates an existing peer.
     *
     * @param peer the peer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated peer,
     * or with status 400 (Bad Request) if the peer is not valid,
     * or with status 500 (Internal Server Error) if the peer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/peers")
    @Timed
    public ResponseEntity<Peer> updatePeer(@Valid @RequestBody Peer peer) throws URISyntaxException {
        log.debug("REST request to update Peer : {}", peer);
        if (peer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Peer result = peerService.save(peer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, peer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /peers : get all the peers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of peers in body
     */
    @GetMapping("/peers")
    @Timed
    public List<Peer> getAllPeers() {
        log.debug("REST request to get all Peers");
        return peerService.findAll();
    }

    /**
     * GET  /peers/:id : get the "id" peer.
     *
     * @param id the id of the peer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the peer, or with status 404 (Not Found)
     */
    @GetMapping("/peers/{id}")
    @Timed
    public ResponseEntity<Peer> getPeer(@PathVariable Long id) {
        log.debug("REST request to get Peer : {}", id);
        Optional<Peer> peer = peerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peer);
    }

    /**
     * DELETE  /peers/:id : delete the "id" peer.
     *
     * @param id the id of the peer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/peers/{id}")
    @Timed
    public ResponseEntity<Void> deletePeer(@PathVariable Long id) {
        log.debug("REST request to delete Peer : {}", id);
        peerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/peers?query=:query : search for the peer corresponding
     * to the query.
     *
     * @param query the query of the peer search
     * @return the result of the search
     */
    @GetMapping("/_search/peers")
    @Timed
    public List<Peer> searchPeers(@RequestParam String query) {
        log.debug("REST request to search Peers for query {}", query);
        return peerService.search(query);
    }

}

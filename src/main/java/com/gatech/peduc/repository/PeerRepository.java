package com.gatech.peduc.repository;

import com.gatech.peduc.domain.Peer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Peer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeerRepository extends JpaRepository<Peer, Long> {

}

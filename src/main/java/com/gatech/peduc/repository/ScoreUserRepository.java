package com.gatech.peduc.repository;

import com.gatech.peduc.domain.ScoreUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ScoreUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoreUserRepository extends JpaRepository<ScoreUser, Long> {

}

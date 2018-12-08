package com.gatech.peduc.repository;

import java.util.List;

import com.gatech.peduc.domain.ScoreUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




/**
 * Spring Data  repository for the ScoreUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoreUserRepository extends JpaRepository<ScoreUser, Long> {
    @Query("select scoreUser from ScoreUser scoreUser where scoreUser.user.id != :id AND scoreUser.user.id is not null AND scoreUser.description != 'DONE'")
    List<ScoreUser> findByUserIsNotCurrentUser(@Param("id") Long  id);

}

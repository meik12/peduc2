package com.gatech.peduc.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.gatech.peduc.domain.Score;
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
    @Query("select scoreUser from ScoreUser scoreUser left join Lecture l on l.id = scoreUser.lecture.id where scoreUser.user.id != :id AND scoreUser.user.id is not null AND l.presentationDate < :z AND scoreUser.description = null")
    List<ScoreUser> findByUserIsNotCurrentUser(@Param("id") Long id, @Param("z") ZonedDateTime  z);
    
    @Query("select new ScoreUser (sum(scoreUser.excellent) as excellent, sum(scoreUser.veryGood) as veryGood, sum(scoreUser.fair) as fair, sum(scoreUser.bad) as bad) FROM ScoreUser scoreUser where scoreUser.user.id = :id AND scoreUser.description = 'DONE'")
    List<ScoreUser> getAverageScore(@Param("id") Long id);
  
}

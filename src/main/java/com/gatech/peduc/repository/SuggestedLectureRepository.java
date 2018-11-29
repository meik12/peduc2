package com.gatech.peduc.repository;

import com.gatech.peduc.domain.SuggestedLecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.websocket.server.PathParam;

/**
 * Spring Data  repository for the SuggestedLecture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuggestedLectureRepository extends JpaRepository<SuggestedLecture, Long> {
    
    @Query("select suggested_lecture from SuggestedLecture suggested_lecture where suggested_lecture.user.id != :id")
    Page<SuggestedLecture> findByUserIsCurrentUser(@Param("id") Long  id, Pageable pageable);

}

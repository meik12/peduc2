package com.gatech.peduc.repository;

import com.gatech.peduc.domain.Lecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Lecture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select lecture from Lecture lecture where lecture.user.id = :id AND lecture.status = 'ACTIVE'")
    Page<Lecture> findByUserIsCurrentUser(@Param("id") Long  id, Pageable pageable);


    @Query("select lecture from Lecture lecture where lecture.user.id != :id AND lecture.user.id is not null AND lecture.status = 'ACTIVE'")
    Page<Lecture> findByUserIsNotCurrentUser(@Param("id") Long  id, Pageable pageable);

}

package com.gatech.peduc.repository;

import com.gatech.peduc.domain.Lecture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Lecture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select lecture from Lecture lecture where lecture.user.login = ?#{principal.username}")
    List<Lecture> findByUserIsCurrentUser();

}

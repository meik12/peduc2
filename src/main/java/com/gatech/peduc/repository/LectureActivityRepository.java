package com.gatech.peduc.repository;

import com.gatech.peduc.domain.LectureActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the LectureActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LectureActivityRepository extends JpaRepository<LectureActivity, Long> {

    @Query(value = "select distinct lecture_activity from LectureActivity lecture_activity left join fetch lecture_activity.users",
        countQuery = "select count(distinct lecture_activity) from LectureActivity lecture_activity")
    Page<LectureActivity> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct lecture_activity from LectureActivity lecture_activity left join fetch lecture_activity.users")
    List<LectureActivity> findAllWithEagerRelationships();

    @Query("select lecture_activity from LectureActivity lecture_activity left join fetch lecture_activity.users where lecture_activity.id =:id")
    Optional<LectureActivity> findOneWithEagerRelationships(@Param("id") Long id);

}

package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {

    @Query("""
            SELECT COUNT(l) > 0 FROM LessonEntity l
            WHERE l.organization.id = :org
            AND  l.employee.id = :empl
            AND l.type IN :types
            AND l.lessonEndTime > :periodStart
            AND l.lessonDateTime < :periodEnd
            AND l.lessonStatus NOT IN :inactiveStatuses
            AND (:id IS NULL OR l.id <> :id)
            """)
    boolean existsOverlayOfLessons(
            @Param("org") Long organizationId,
            @Param("empl") Long employeeId,
            @Param("types") List<LessonType> checkTypes,
            @Param("periodStart") LocalDateTime startLessonTime,
            @Param("periodEnd") LocalDateTime endLessonTime,
            @Param("inactiveStatuses") List<LessonStatus> inactiveStatuses,
            @Param("id") Long id
            );



    @Query("""
            SELECT
            l.id AS id,
            l.lessonStatus AS lessonStatus,
            l.type AS type,
            l.category AS category,
            l.lessonDateTime AS lessonDateTime,
            l.lessonEndTime AS lessonEndTime,
            l.comments AS comments,
            l.serviceType.id AS serviceType,
            l.organization.id AS organization,
            l.employee.id AS employee,
            l.child.id AS child
            FROM LessonEntity l
            WHERE l.lessonDateTime >= :start
            AND l.lessonDateTime <= :end
            AND l.lessonStatus NOT IN :ignoreStatuses
            AND l.organization.id = :orgId
            """)
    List<LessonResponseProjection> findAllByOrgIdForPeriod(
            @Param("orgId") Long organizationId,
            @Param("start")LocalDateTime startDate,
            @Param("end") LocalDateTime endDate,
            @Param("ignoreStatuses") List<LessonStatus> ignoreStatuses);


}

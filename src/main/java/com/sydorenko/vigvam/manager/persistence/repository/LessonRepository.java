package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {

    @Query("""
            SELECT COUNT(l) > 0 FROM LessonEntity l
            WHERE l.organization.id = :org
            AND  l.employee.id = :empl
            AND l.lessonType IN :types
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
            l.lessonType AS lessonType,
            l.category AS category,
            l.lessonDateTime AS lessonDateTime,
            l.lessonEndTime AS lessonEndTime,
            l.comments AS comments,
            l.serviceType.id AS serviceTypeId,
            l.organization.id AS organizationId,
            l.employee.id AS employeeId,
            l.child.id AS childId,
            l.updatedLessonStatus AS updatedLessonStatus
            FROM LessonEntity l
            WHERE l.lessonDateTime >= :start
            AND l.lessonDateTime <= :end
            AND l.lessonStatus NOT IN :ignoreStatuses
            AND l.organization.id = :orgId
            """)
    List<LessonResponseProjection> findAllByOrgIdForPeriod(
            @Param("orgId") Long organizationId,
            @Param("start") LocalDateTime startDate,
            @Param("end") LocalDateTime endDate,
            @Param("ignoreStatuses") List<LessonStatus> ignoreStatuses);


    @Query("""
            SELECT COUNT(l) > 0 FROM LessonEntity l
            WHERE l.organization.id = :org
            AND l.lessonEndTime > :startDateTime
            AND l.lessonDateTime < :endDateTime
            AND l.category = :lessonCategory
            AND l.lessonStatus NOT IN :inactiveStatuses
            """)
    boolean existsCategoriesLessonsThisDay(
            @Param("org") Long organizationId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("lessonCategory") LessonCategory lessonCategory,
            @Param("inactiveStatuses") List<LessonStatus> ignoreStatuses);


    @Query("""
            SELECT l
            FROM LessonEntity l
            WHERE l.lessonDateTime >= :start
            AND l.lessonDateTime <= :end
            AND l.lessonStatus NOT IN :ignoreStatuses
            AND l.organization.id = :orgId
            """)
    List<LessonEntity> findAllEntityByOrgIdForPeriod(
            @Param("orgId") Long organizationId,
            @Param("start") LocalDateTime startDate,
            @Param("end") LocalDateTime endDate,
            @Param("ignoreStatuses") List<LessonStatus> ignoreStatuses);

}

package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface PlanningLessonRepository extends JpaRepository<PlanningLessonEntity,Long> {

    @Query("""
            SELECT COUNT(l) > 0 FROM PlanningLessonEntity l
            WHERE l.organization.id = :org
            AND  l.employee.id = :empl
            AND l.lessonType IN :types
            AND l.lessonEndTime > :periodStart
            AND l.lessonTime < :periodEnd
            AND l.lessonDayOfWeek = :lessonDayOfWeek
            AND l.lessonStatus NOT IN :inactiveStatuses
            AND (:id IS NULL OR l.id <> :id)
            AND (:parentPlanId IS NULL OR l.id <> :parentPlanId)
            """)
    boolean existsOverlayOfLessons(@Param("org") Long organizationId,
                                   @Param("empl") Long employeeId,
                                   @Param("types") List<LessonType> checkTypes,
                                   @Param("periodStart") LocalTime startLessonTime,
                                   @Param("periodEnd") LocalTime endLessonTime,
                                   @Param("lessonDayOfWeek") DayOfWeek dayOfWeek,
                                   @Param("inactiveStatuses") List<LessonStatus> inactiveStatuses,
                                   @Param("id") Long id,
                                   @Param("parentPlanId") Long parentPlanId);

    @Query("""
            SELECT l FROM PlanningLessonEntity l
            WHERE l.lessonDayOfWeek IN :dayOfWeekSet
            AND l.lessonStatus NOT IN :ignoreStatuses
            AND l.organization.id = :orgId
            """)
    List<PlanningLessonEntity> findAllByOrgIdForPeriod(
            @Param("orgId") Long organizationId,
            @Param("dayOfWeekSet") Set<DayOfWeek> dayOfWeekSet,
            @Param("ignoreStatuses") List<LessonStatus> ignoreLessonStatusesInSchedule);

    List<PlanningLessonEntity> findAllByLessonDayOfWeekAndOrganizationIdAndLessonStatusNotIn(DayOfWeek dayOfWeek, Long organizationId, List<LessonStatus> ignoreLessonStatusesInSchedule);

    List<PlanningLessonEntity> findAllByIdIn(Set<Long> idList);
}


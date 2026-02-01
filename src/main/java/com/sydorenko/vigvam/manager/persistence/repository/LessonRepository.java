package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
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
            WHERE l.organization = :org
            AND  l.employee = :empl
            AND l.type IN :types
            AND l.lessonEndTime > :yourStart
            AND l.lessonDateTime < :yourEnd
            AND l.lessonStatus NOT IN :inactiveStatuses
            AND (:id IS NULL OR l.id <> :id)
            """)
    boolean existsOverlayOfLessons(
            @Param("org") OrganizationEntity organization,
            @Param("empl") EmployeeEntity employee,
            @Param("types") List<LessonType> checkTypes,
            @Param("yourStart") LocalDateTime startLessonTime,
            @Param("yourEnd") LocalDateTime endLessonTime,
            @Param("inactiveStatuses") List<LessonStatus> inactiveStatuses,
            @Param("id") Long id
            );
}

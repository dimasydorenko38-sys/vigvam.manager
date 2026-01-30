package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    boolean existsByOrganizationAndEmployeeAndLessonDateTimeAndType(OrganizationEntity organization, EmployeeEntity employee, LocalDateTime lessonDateTime, LessonType type);
}

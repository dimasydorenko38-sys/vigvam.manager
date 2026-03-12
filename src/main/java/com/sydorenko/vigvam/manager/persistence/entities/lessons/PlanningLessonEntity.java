package com.sydorenko.vigvam.manager.persistence.entities.lessons;

import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "planning_lessons", indexes = {
        @Index(name = "idx_planninglessonentity", columnList = "organization_id, employee_id")
})
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PlanningLessonEntity extends AbstractLessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_time", nullable = false)
    private LocalTime lessonTime;

    @Column(name = "lesson_end_time", nullable = false)
    private LocalTime lessonEndTime;

    @Column(name = "lesson_day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek lessonDayOfWeek;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdById;

    @LastModifiedBy
    @Column(name = "last_modified_by_id")
    private Long lastModifiedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false, columnDefinition = "DATE")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "DATE")
    private LocalDateTime updatedAt;

    @Override
    public LocalTime getStartTimeForChecking() {
        return lessonTime.truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public LocalTime getEndTimeForChecking() {
        return lessonEndTime != null ? lessonEndTime.truncatedTo(ChronoUnit.MINUTES) : null;
    }

    @Override
    public void setEndTimeForChecking(LocalTime endTime) {
        this.lessonEndTime = endTime;
    }
}

package com.sydorenko.vigvam.manager.persistence.entities.lessons;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lessons", indexes = {
        @Index(name = "idx_lessonentity", columnList = "organization_id, lesson_date_time, employee_id")
})
public class LessonEntity extends AbstractLessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_date_time", nullable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lessonDateTime;

    @Column(name = "lesson_end_time", nullable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lessonEndTime;

//    TODO: create  Child -> (OneToMany) childPerformanceEntity (ManyToOne) -> Lessons
    // childPerformanceEntity: id/ stars / comments / entity_id /


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
        return lessonDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public LocalTime getEndTimeForChecking() {
        return lessonEndTime != null ? lessonEndTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES) : null;
    }

    @Override
    public void setEndTimeForChecking(LocalTime endTime) {
        this.lessonEndTime = this.lessonDateTime.toLocalDate().atTime(endTime);
    }
}

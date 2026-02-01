package com.sydorenko.vigvam.manager.persistence.entities.lessons;

import com.sydorenko.vigvam.manager.interfaces.Statusable;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lessons", indexes = {
        @Index(name = "lesson_organization_id_idx", columnList = "organization_id")
})
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private LessonStatus lessonStatus;

    @Column(name = "updated_lesson_status")
    private LocalDateTime updatedLessonStatus;

    @ManyToOne
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "service_type", nullable = false)
    private ServiceTypeEntity serviceType;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private LessonType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private LessonCategory category;

    @Column(name = "lesson_date_time", nullable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lessonDateTime;

    @Column(name = "lesson_end_time", nullable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lessonEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "organization", nullable = false)
    private OrganizationEntity organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "employee", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "child")
    private ChildEntity child;

//    TODO: create  Child OneToMany childPerformanceEntity  ManyToOne Lessons
    // childPerformanceEntity: id/ stars / comments / entity_id /

    @Column(name = "comments")
    private String comments;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdById;

    @LastModifiedBy
    @Column(name = "last_modified_by_id")
    private Long lastModifiedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "DATE")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATE")
    private LocalDateTime updatedAt;

}

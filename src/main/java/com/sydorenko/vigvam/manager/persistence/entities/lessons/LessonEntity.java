package com.sydorenko.vigvam.manager.persistence.entities.lessons;

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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Audited
@Table(name = "lessons", indexes = {
        @Index(name = "lesson_organization_id_idx", columnList = "organization_id")
})
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private LessonStatus status;

    @ManyToOne
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceTypeEntity serviceType;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private LessonType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private LessonCategory category;

    @Column(name = "lesson_date_time", nullable = false)
    private LocalDateTime lessonDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildEntity child;

    @Column(name = "comments")
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "DATE")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATE")
    private LocalDateTime updatedAt;

}

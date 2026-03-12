package com.sydorenko.vigvam.manager.persistence.entities.lessons;

import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractLessonEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_status",nullable = false)
    private LessonStatus lessonStatus;

    @Column(name = "updated_lesson_status")
    private LocalDateTime updatedLessonStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceTypeEntity serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private LessonType lessonType;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private LessonCategory category;

    @Column(name = "parent_plan_id")
    private Long parentPlanId;

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
    @JoinColumn(name = "child_id")
    private ChildEntity child;

    @Column(name = "comments")
    private String comments;


    public abstract LocalTime getStartTimeForChecking();
    public abstract LocalTime getEndTimeForChecking();
    public abstract void setEndTimeForChecking(LocalTime endTime);
}

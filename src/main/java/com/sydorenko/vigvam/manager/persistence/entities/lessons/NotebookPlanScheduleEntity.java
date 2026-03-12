package com.sydorenko.vigvam.manager.persistence.entities.lessons;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "notebook_plan_schedule")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotebookPlanScheduleEntity implements Statusable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", updatable = false, nullable = false)
    private ChildEntity child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id",nullable = false)
    private OrganizationEntity organization;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "lesson_type_list")
    @Enumerated(EnumType.STRING)
    private List<LessonType> lessonTypeList;

    @Column(name = "service_type_ids")
    private Set<Long> serviceTypeIds;

    @Column(name = "message", nullable = false)
    private String message;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "disable_date")
    private LocalDateTime disableDate;
}

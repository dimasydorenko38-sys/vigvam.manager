package com.sydorenko.vigvam.manager.entities.lessons;

import com.sydorenko.vigvam.manager.entities.organizations.Organization;
import com.sydorenko.vigvam.manager.entities.users.Child;
import com.sydorenko.vigvam.manager.entities.users.Specialist;
import com.sydorenko.vigvam.manager.enums.ServicesForPrice;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.StatusLessons;
import com.sydorenko.vigvam.manager.enums.lessons.TypeLessons;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter @Setter
@NoArgsConstructor
@Audited
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_dates", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private StatusLessons status;

    @Enumerated(EnumType.STRING)
    @Column(name = "services")
    private ServicesForPrice service;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private TypeLessons type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private LessonCategory category;

    @Column(name = "dates", nullable = false)
    private LocalDate date;

    @Column (name = "time",nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(name = "commentations")
    private String commentation;

}

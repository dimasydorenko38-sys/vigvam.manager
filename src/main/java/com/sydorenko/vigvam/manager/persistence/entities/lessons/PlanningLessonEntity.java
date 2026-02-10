package com.sydorenko.vigvam.manager.persistence.entities.lessons;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "planning_lessons")
public class PlanningLessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "service", nullable = false)
    private ServiceTypeEntity serviceType;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private LessonType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private LessonCategory category;

    @Column(name = "lesson_time", nullable = false)
    private LocalTime lessonTime;

    @Column(name = "lesson_day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek lessonDayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildEntity child;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATE")
    private LocalDate updatedAt;
}

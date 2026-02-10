package com.sydorenko.vigvam.manager.persistence.entities.organizations;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "setting_lessons_time")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SettingLessonsTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_hour_of_work", nullable = false)
    private LocalTime firstHourOfWork;

    @Column(name = "last_hour_of_work", nullable = false)
    private LocalTime lastHourOfWork;

    @Column(name = "lesson_duration", nullable = false)
    private Long lessonDurationMinutes;

    @Column(name = "break_duration", nullable = false)
    private Long breakDurationMinutes;

    @Column(name = "lesson_type")
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "organization_id", nullable = false)
    private OrganizationEntity organization;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdById;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by_id")
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;


    public SettingLessonsTime(LocalTime firstHourOfWork, LocalTime lastHourOfWork, Long lessonDurationMinutes, Long breakDurationMinutes, OrganizationEntity organization) {
        this.firstHourOfWork = firstHourOfWork;
        this.lastHourOfWork = lastHourOfWork;
        this.lessonDurationMinutes = lessonDurationMinutes;
        this.breakDurationMinutes = breakDurationMinutes;
        this.organization = organization;
    }
}

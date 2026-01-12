package com.sydorenko.vigvam.manager.persistence.entities.organizations;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.LongToIntFunction;

@Entity
@Table(name = "setting_lessons_time")
@Getter
@Setter
@NoArgsConstructor
public class SettingLessonsTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "first_hour_of_work", nullable = false)
    private LocalTime firstHourOfWork;

    @Column(name = "last_hour_of_work", nullable = false)
    private LocalTime lastHourOfWork;

    @Column(name = "lesson_duration", nullable = false)
    private Long lessonDuration;

    @Column(name = "break_duration", nullable = false)
    private Long breakDuration;

    @Column(name = "lesson_type")
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @ManyToOne
    @JoinColumn (name = "organization_id")
    private OrganizationEntity organization;
}

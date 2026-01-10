package com.sydorenko.vigvam.manager.persistence.entities.organizations;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public class SettingLessonsTime {

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
}

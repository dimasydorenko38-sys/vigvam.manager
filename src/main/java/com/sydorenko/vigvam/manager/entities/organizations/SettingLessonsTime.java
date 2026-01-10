package com.sydorenko.vigvam.manager.entities.organizations;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public class SettingLessonsTime {

    @CreationTimestamp
    @Column(name = "created_dates", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "first_hour_of_work", nullable = false)
    private LocalTime firstHourOfWork;

    @Column(name = "last_hour_of_work", nullable = false)
    private LocalTime lastHourOfWork;

    @Column(name = "lesson_duration", nullable = false)
    private LocalTime lessonDuration;

    @Column(name = "break_duration", nullable = false)
    private LocalTime breakDuration;
}

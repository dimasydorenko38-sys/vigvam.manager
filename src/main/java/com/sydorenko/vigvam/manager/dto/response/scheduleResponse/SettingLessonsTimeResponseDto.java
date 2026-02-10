package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SettingLessonsTimeResponseDto {
    private LocalTime firstHourOfWork;
    private LocalTime lastHourOfWork;
    private Long lessonDurationMinutes;
    private Long breakDurationMinutes;
    private LessonType lessonType;



    public SettingLessonsTimeResponseDto(SettingLessonsTime setting){
        this.firstHourOfWork = setting.getFirstHourOfWork();
        this.lastHourOfWork = setting.getLastHourOfWork();
        this.lessonDurationMinutes = setting.getLessonDurationMinutes();
        this.breakDurationMinutes = setting.getBreakDurationMinutes();
        this.lessonType = setting.getLessonType();
    }
}

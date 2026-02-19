package com.sydorenko.vigvam.manager.dto.request;

import java.time.LocalTime;

public record CreateSettingLessonsTimeRequestDto (
    String lessonType,
    LocalTime firstHourOfWork,
    LocalTime lastHourOfWork,
    Long lessonDurationMinutes,
    Long breakDurationMinutes
){}

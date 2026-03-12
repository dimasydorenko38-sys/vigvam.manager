package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;

import java.time.LocalTime;

public record ConflictResponseDto(
        LocalTime startTime,
        Long employeeId,
        Long planId,
        Long factId,
        LessonType lessonType,
        String message
) {
}

package com.sydorenko.vigvam.manager.dto.request.lessons;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record PlanningLessonRequestDto(

        Long id,
        String status,
        @NonNull Long serviceTypeId,
        @NonNull String type,
        @NonNull LocalTime lessonTime,
        LocalTime lessonEndTime,
        @NonNull DayOfWeek lessonDayOfWeek,
        @NonNull Long organizationId,
        @NonNull Long employeeId,
        String comments,
        Long childId

) {
}

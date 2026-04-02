package com.sydorenko.vigvam.manager.dto.request.lessons;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record CreatePlanningLessonRequestDto(

        Long id,
        String status,
        @NonNull Long serviceTypeId,
        @NotBlank(message = "Тип уроку обовʼязковий") String type,
        @NonNull LocalTime lessonTime,
        LocalTime lessonEndTime,
        @NonNull DayOfWeek lessonDayOfWeek,
        @NonNull Long organizationId,
        @NonNull Long employeeId,
        String comments,
        Long childId

) {
}

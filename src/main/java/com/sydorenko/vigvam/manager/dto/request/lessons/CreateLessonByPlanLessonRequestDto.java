package com.sydorenko.vigvam.manager.dto.request.lessons;

import lombok.NonNull;

import java.time.LocalDate;

public record CreateLessonByPlanLessonRequestDto(
        @NonNull Long planLessonId,
        @NonNull LocalDate date
) {
}

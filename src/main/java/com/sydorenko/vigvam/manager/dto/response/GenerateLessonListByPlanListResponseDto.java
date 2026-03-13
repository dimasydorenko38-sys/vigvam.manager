package com.sydorenko.vigvam.manager.dto.response;

import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record GenerateLessonListByPlanListResponseDto(
        @NonNull LocalDate date,
        @NonNull Long organizationId,
        Set<Long> planLessonIds
) {
    public GenerateLessonListByPlanListResponseDto{
        if (planLessonIds == null) {
            planLessonIds = Set.of();
        }
    }
}

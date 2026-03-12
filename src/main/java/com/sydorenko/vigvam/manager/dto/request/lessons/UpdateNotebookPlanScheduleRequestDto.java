package com.sydorenko.vigvam.manager.dto.request.lessons;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UpdateNotebookPlanScheduleRequestDto(
        @NonNull Long id,
        @NonNull LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> lessonTypeList,
        Set<Long> serviceTypeIds,
        @NonNull String message
) {
}

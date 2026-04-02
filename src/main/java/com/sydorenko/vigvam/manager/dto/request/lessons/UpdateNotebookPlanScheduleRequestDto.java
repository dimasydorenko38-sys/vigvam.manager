package com.sydorenko.vigvam.manager.dto.request.lessons;

import jakarta.validation.constraints.NotBlank;
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
        @NotBlank(message = "Коментар не можу бути порожнім") String message
) {
}

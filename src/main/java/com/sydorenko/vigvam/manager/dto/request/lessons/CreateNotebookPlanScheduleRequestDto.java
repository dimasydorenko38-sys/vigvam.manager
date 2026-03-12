package com.sydorenko.vigvam.manager.dto.request.lessons;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record CreateNotebookPlanScheduleRequestDto(
        @NonNull Long childId,
        @NonNull Long organizationId,
        @NonNull LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> lessonTypeList,
        Set<Long> serviceTypeIds,
        @NonNull String message
) {
    public List<String> lessonTypeList() {
        return lessonTypeList != null ? lessonTypeList : List.of();
    }
    public Set<Long> serviceTypeIds(){
        return serviceTypeIds != null ? serviceTypeIds : Set.of();
    }
}

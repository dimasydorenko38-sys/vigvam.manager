package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import java.util.List;
import java.util.Set;

public record PlanDayScheduleResponseDto(
        Set<Long> employeeIds,
        List<PlanningLessonResponseDto> lessons
) {

}

package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import java.util.List;
import java.util.Set;

public record DayScheduleResponseDto (
        Set<Long> employeeIds,
        List<LessonResponseProjection> lessons){

}

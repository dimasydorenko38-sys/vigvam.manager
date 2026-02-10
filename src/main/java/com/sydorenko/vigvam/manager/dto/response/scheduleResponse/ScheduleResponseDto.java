package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScheduleResponseDto {
    private OrganizationResponseDto organization;
    private Set<EmployeeNameResponseProjection> employees;
    private List<LessonResponseProjection> lessons;
}

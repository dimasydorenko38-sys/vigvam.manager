package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlanScheduleResponseDto {
    private OrganizationResponseDto organization;
    private Set<EmployeeNameResponseProjection> employeesData;
    private Map<DayOfWeek, PlanDayScheduleResponseDto> schedule;
}

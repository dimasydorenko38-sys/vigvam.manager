package com.sydorenko.vigvam.manager.dto.request.lessons;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetScheduleRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long organizationId;
}

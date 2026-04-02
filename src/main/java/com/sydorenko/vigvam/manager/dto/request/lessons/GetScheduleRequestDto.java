package com.sydorenko.vigvam.manager.dto.request.lessons;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetScheduleRequestDto {
    @NonNull private LocalDate startDate;
    @NonNull private LocalDate endDate;
    @NonNull private Long organizationId;
}

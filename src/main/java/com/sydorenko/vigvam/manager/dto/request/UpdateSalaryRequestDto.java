package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.dto.request.users.CreateSalaryEmployeeRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateSalaryRequestDto(
        Long contractId,
        LocalDateTime activatedDate,
        List<CreateSalaryEmployeeRequestDto> salary
) {
}

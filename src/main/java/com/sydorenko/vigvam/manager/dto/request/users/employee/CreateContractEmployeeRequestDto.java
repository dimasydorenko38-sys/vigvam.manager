package com.sydorenko.vigvam.manager.dto.request.users.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContractEmployeeRequestDto {

    LocalDate activatedDate;
    @NotBlank
    private Long employeeId;
    @NotBlank
    private Long organizationId;

    private Long masterEmployeeId;
    @NotEmpty
    private List<CreateSalaryEmployeeRequestDto> salary;
    @NotBlank
    private String role;

}

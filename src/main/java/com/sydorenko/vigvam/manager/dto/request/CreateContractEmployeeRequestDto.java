package com.sydorenko.vigvam.manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContractEmployeeRequestDto {

    @NonNull
    private Long employeeId;
    @NonNull
    private Long organizationId;

    private Long masterEmployeeId;
    @NonNull
    private List<CreateSalaryEmployeeRequestDto> salary;
    @NonNull
    private String role;

}

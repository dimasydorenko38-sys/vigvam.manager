package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.service.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ContractEmployeeService contractEmployeeService;


    @PostMapping("/add")
    public ResponseEntity<AuthResponseDto> createEmployee(@RequestBody CreateEmployeeRequestDto dto) {
        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    @PostMapping("/contracts/add")
    private void createContractEmployee(@RequestBody CreateContractEmployeeRequestDto dto){
        contractEmployeeService.createContract(dto);
    }
}

package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.UpdateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateSalaryRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.usersServices.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<AuthResponseDto> createEmployee(@RequestBody CreateEmployeeRequestDto dto) {
        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<MessageResponseDto> updateEmployee (@RequestBody UpdateEmployeeRequestDto dto){
        employeeService.updateEmployee(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableEmployee(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        employeeService.setDisableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableEmployee(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        employeeService.setEnableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

}

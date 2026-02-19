package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.usersServices.EmployeeService;
import lombok.RequiredArgsConstructor;
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
    private final ContractEmployeeService contractEmployeeService;
    private final MessageResponseDto messageResponseDto;


    @PostMapping("/add")
    public ResponseEntity<AuthResponseDto> createEmployee(@RequestBody CreateEmployeeRequestDto dto) {
        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/contracts/add")
    public ResponseEntity<MessageResponseDto> createContractEmployee(@RequestBody CreateContractEmployeeRequestDto dto){
        contractEmployeeService.createContract(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableEmployee(@RequestBody NewStatusObjectByIdRequestDto dto){
        employeeService.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableEmployee(@RequestBody NewStatusObjectByIdRequestDto dto){
        employeeService.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }
}

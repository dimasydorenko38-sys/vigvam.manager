package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.UpdateSalaryRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.usersServices.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract")
public class ContractEmployeeController {

    private final ContractEmployeeService contractEmployeeService;
    private final SalaryService salaryService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponseDto> createContractEmployee(@RequestBody CreateContractEmployeeRequestDto dto){
        contractEmployeeService.createContract(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<MessageResponseDto> updateSalaryByContract(@RequestBody UpdateSalaryRequestDto dto){
        contractEmployeeService.updateSalaryByContract(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableContract(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        contractEmployeeService.setDisableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableContract(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        contractEmployeeService.setEnableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/salary/disable")
    public ResponseEntity<MessageResponseDto> disableSalary(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        salaryService.setDisableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/salary/enable")
    public ResponseEntity<MessageResponseDto> enableSalary(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        salaryService.setEnableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }
}

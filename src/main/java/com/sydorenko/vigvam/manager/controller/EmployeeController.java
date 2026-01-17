package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.service.EmployeeService;
import lombok.RequiredArgsConstructor;
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
    public void createEmployee(@RequestBody CreateEmployeeRequestDto dto) {
        employeeService.createEmployee(dto);
    }
}

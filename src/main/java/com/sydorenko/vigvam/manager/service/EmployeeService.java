package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public void createEmployee(CreateEmployeeRequestDto dto) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setLogin(dto.getLogin());
        employee.setPassword(dto.getPassword());
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhone(dto.getPhone());
        employee.setStatus(Status.ENABLED);

        employeeRepository.save(employee);
    }
}

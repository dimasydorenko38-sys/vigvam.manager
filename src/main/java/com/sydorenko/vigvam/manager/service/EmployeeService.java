package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;

    public AuthResponseDto createEmployee(@NonNull CreateEmployeeRequestDto dto) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setLogin(dto.getLogin());
        employee.setPassword(dto.getPassword());
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhone(dto.getPhone());
        employee.setBirthday(dto.getBirthday());
        employee.setStatus(Status.ENABLED);
        EmployeeEntity employeeSave = employeeRepository.save(employee);
        UUID refreshToken = employeeSave.getRefreshToken();
        String token = jwtService.generateToken(employeeSave);
        return new AuthResponseDto(token,refreshToken);
    }
}

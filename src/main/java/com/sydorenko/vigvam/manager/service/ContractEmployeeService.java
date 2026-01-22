package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ContractEmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ContractEmployeeService {

    private final ContractEmployeeRepository contractEmployeeRepository;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceTypeService serviceTypeService;

    public void createContract(CreateContractEmployeeRequestDto dto) {
        ContractEmployeeEntity contractEmployee = new ContractEmployeeEntity();

        contractEmployee.setEmployee(employeeRepository.findById(dto.getEmployee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployee().getId())));
        contractEmployee.setOrganization(organizationRepository.findById(dto.getOrganization().getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getOrganization().getId())));
        if (dto.getMasterEmployee() != null) {
            contractEmployee.setMasterEmployee(employeeRepository.findById(dto.getMasterEmployee().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getMasterEmployee().getId())));
        }
        contractEmployee.setSalary(dto.getSalary()
                .stream()
                .peek(salary -> {
                    salary.setContractEmployee(contractEmployee);
                    salary.setServiceType(serviceTypeService.getServiceTypeById(salary.getServiceType().getId()));
                        }
                ).collect(Collectors.toSet()));
        contractEmployee.setRole(RoleUser.fromString(dto.getRole()));
        contractEmployee.setStatus(Status.ENABLED);

        contractEmployeeRepository.save(contractEmployee);
    }
}


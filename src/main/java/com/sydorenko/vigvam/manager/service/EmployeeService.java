package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.enums.users.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.RoleEntity;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;

    public void createEmployee(CreateEmployeeRequestDto dto) {
        List<OrganizationEntity> organizations = organizationRepository.findAllById(dto.getOrganizationIds());
        Set<RoleEntity> roles = roleRepository.findAll().stream()
                .filter(r -> dto.getRoles().contains(r.getRole()))
                .collect(Collectors.toSet());
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .phone(dto.getPhone())
                .name(dto.getName())
                .organizations(new HashSet<>(organizations))
                .roles(roles)
                .status(Status.ENABLED)
                .build();

        employeeRepository.save(employeeEntity);
    }
}

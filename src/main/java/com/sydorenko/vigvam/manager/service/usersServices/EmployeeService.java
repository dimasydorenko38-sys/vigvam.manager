package com.sydorenko.vigvam.manager.service.usersServices;

import com.sydorenko.vigvam.manager.dto.request.users.employee.UpdateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.employee.CreateEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.EmployeeNameResponseProjection;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ContractEmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.JwtService;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService extends StatusableService<EmployeeEntity> {

    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;
    private final GenericService genericService;
    private final ContractEmployeeRepository contractEmployeeRepository;
    private final ContractEmployeeService contractEmployeeService;

    public AuthResponseDto createEmployee(@NonNull CreateEmployeeRequestDto dto) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setLogin(dto.getLogin().toLowerCase());
        employee.setPassword(dto.getPassword());
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhone(genericService.formatPhone(dto.getPhone()));
        employee.setBirthday(dto.getBirthday());
        employee.setStatus(Status.ENABLED);
        EmployeeEntity employeeSave = employeeRepository.save(employee);
        UUID refreshToken = employeeSave.getRefreshToken();
        String token = jwtService.generateToken(employeeSave);
        return new AuthResponseDto(token,refreshToken);
    }

    public void updateEmployee (UpdateEmployeeRequestDto dto){
        EmployeeEntity employee = employeeRepository.findActiveById(dto.getEmployeeId())
                .orElseThrow(()-> new EntityNotFoundException("Профіль не активний або не існує"));
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setPhone(genericService.formatPhone(dto.getPhone()));
        employee.setBirthday(dto.getBirthday());
        employeeRepository.save(employee);
    }

    public void setDisableStatusCascade(Long employeeId) {
        List<ContractEmployeeEntity> contractRefList = contractEmployeeRepository.getReferenceByEmployeeIdAndStatus(employeeId, Status.ENABLED);
        if(!contractRefList.isEmpty()){
            contractRefList.forEach(contract -> contractEmployeeService.setDisableStatus(contract.getId()));
        }
        super.setDisableStatus(employeeId, employeeRepository);
    }

    public void setEnableStatus(Long employeeId) {
        super.setEnableStatus(employeeId, employeeRepository);
    }

    public Set<OrganizationEntity> getAllOrganizations(@NonNull EmployeeEntity employee){
        Set<ContractEmployeeEntity> contracts = employee.getContractsEmployee()
                .stream()
                .filter(contract -> Status.ENABLED.equals(contract.getStatus()))
                .collect(Collectors.toSet());
        if(contracts.isEmpty()){
            throw new EntityNotFoundException("Не знайдено жодного активного контракту");
        }
        Set<OrganizationEntity> organizations = contracts.stream()
                .map(ContractEmployeeEntity::getOrganization)
                .filter(organization -> Status.ENABLED.equals(organization.getStatus()))
                .collect(Collectors.toSet());
        if(!organizations.isEmpty()){
            return organizations;
        } else throw new EntityNotFoundException("Не знайдено активної організації за контрактами працівника");


    }

    public Set<EmployeeNameResponseProjection> getAllEmployeesByIds(Set<Long> employeeIds) {
        return employeeRepository.findAllByIdIn(employeeIds);
    }
}

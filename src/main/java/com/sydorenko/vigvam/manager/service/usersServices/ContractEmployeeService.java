package com.sydorenko.vigvam.manager.service.usersServices;

import com.sydorenko.vigvam.manager.dto.request.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.EmployeeNameResponseProjection;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ContractEmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.organizationsServices.ServiceTypeService;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ContractEmployeeService extends StatusableService<ContractEmployeeEntity> {

    private final ContractEmployeeRepository contractEmployeeRepository;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceTypeService serviceTypeService;

    public void createContract(@NonNull CreateContractEmployeeRequestDto dto) {
        ContractEmployeeEntity contractEmployee = new ContractEmployeeEntity();

        contractEmployee.setEmployee(employeeRepository.findActiveById(dto.getEmployee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Active Employee not found with id: " + dto.getEmployee().getId())));
        contractEmployee.setOrganization(organizationRepository.findActiveById(dto.getOrganization().getId())
                .orElseThrow(() -> new EntityNotFoundException("Active Organization not found with id: " + dto.getOrganization().getId())));
        if (dto.getMasterEmployee() != null) {
            contractEmployee.setMasterEmployee(employeeRepository.findActiveById(dto.getMasterEmployee().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Active Employee not found with id: " + dto.getMasterEmployee().getId())));
        }
        contractEmployee.setSalary(dto.getSalary()
                .stream()
                .peek(salary -> {
                    salary.setContractEmployee(contractEmployee);
                    salary.setServiceType(serviceTypeService.getServiceTypeAndCheck(salary.getServiceType().getId()));
                        }
                ).collect(Collectors.toSet()));
        contractEmployee.setRole(RoleUser.fromString(dto.getRole()));
        contractEmployee.setStatus(Status.ENABLED);
        contractEmployeeRepository.save(contractEmployee);
    }

    public List<EmployeeNameResponseProjection> getAllEmployeeNamesByOrg(Long organizationId){
        return contractEmployeeRepository
                .findAllEmployeesNameByOrganizationId(organizationId, RoleUser.EMPLOYEE, Status.ENABLED);
    }

    public void setDisableStatus(DisabledObjectRequestDto dto) {
        super.setDisableStatus(dto, contractEmployeeRepository);
    }

    public void setEnableStatus(DisabledObjectRequestDto dto) {
        super.setEnableStatus(dto, contractEmployeeRepository);
    }

}


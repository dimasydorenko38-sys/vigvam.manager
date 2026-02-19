package com.sydorenko.vigvam.manager.service.usersServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.dto.request.CreateContractEmployeeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.EmployeeNameResponseProjection;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ContractEmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import com.sydorenko.vigvam.manager.service.organizationsServices.ServiceTypeService;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ContractEmployeeService extends StatusableService<ContractEmployeeEntity> {

    private final ContractEmployeeRepository contractEmployeeRepository;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final SalaryService salaryService;

    public void createContract(CreateContractEmployeeRequestDto dto) {
        boolean existActiveContract = contractEmployeeRepository.existsByEmployeeIdAndOrganizationIdAndRoleAndStatus(dto.getEmployeeId(), dto.getOrganizationId(), RoleUser.fromString(dto.getRole()), Status.ENABLED);
        if (existActiveContract) {
            throw new DuplicateRequestException("Контракт з цією роллю в організації вже існує. Відредагуйте існуючий");
        }
        if (!employeeRepository.existsActiveById(dto.getEmployeeId())) {
            throw new EntityNotFoundException("Active Employee not found with id: " + dto.getEmployeeId());
        }
        if (!organizationRepository.existsActiveById(dto.getOrganizationId())) {
            throw new EntityNotFoundException("Active Organization not found with id: " + dto.getOrganizationId());
        }

        ContractEmployeeEntity contractEmployee = new ContractEmployeeEntity();
        contractEmployee.setEmployee(employeeRepository.getReferenceById(dto.getEmployeeId()));
        contractEmployee.setOrganization(organizationRepository.getReferenceById(dto.getOrganizationId()));
        if (dto.getMasterEmployeeId() != null) {
            if (!employeeRepository.existsActiveById(dto.getMasterEmployeeId())) {
                throw new EntityNotFoundException("Active Employee not found with id: " + dto.getMasterEmployeeId());
            }
            contractEmployee.setMasterEmployee(employeeRepository.getReferenceById(dto.getMasterEmployeeId()));
        }
        contractEmployee.setSalary(salaryService.createSalaryForContract(dto.getSalary(), contractEmployee));
        contractEmployee.setRole(RoleUser.fromString(dto.getRole()));
        contractEmployee.setStatus(Status.ENABLED);
        contractEmployeeRepository.save(contractEmployee);
    }

    public List<EmployeeNameResponseProjection> getAllEmployeeNamesByOrg(Long organizationId) {
        return contractEmployeeRepository
                .findAllEmployeesNameByOrganizationId(organizationId, RoleUser.EMPLOYEE, Status.ENABLED);
    }

    public void setDisableStatus(NewStatusObjectByIdRequestDto dto) {
        super.setDisableStatus(dto.getId(), contractEmployeeRepository);
    }

    public void setEnableStatus(NewStatusObjectByIdRequestDto dto) {
        super.setEnableStatus(dto.getId(), contractEmployeeRepository);
    }

}


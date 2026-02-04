package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.SalaryEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.*;
import com.sydorenko.vigvam.manager.service.organizationsServices.ServiceTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateSuperAdminService {
    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceTypeService serviceTypeService;
    private final ContractEmployeeRepository contractEmployeeRepository;
    private final ServiceTypeRepository serviceTypeRepository;


    public void createSuperAdmin(){
        if(employeeRepository.count() == 0){
            EmployeeEntity employee = new EmployeeEntity();
            employee.setPhone("");
            employee.setName("Super_Admin");
            employee.setLogin("super_admin");
            employee.setLastName("");
            employee.setPassword("super_admin");
            employee.setStatus(Status.ENABLED);
            EmployeeEntity employeeSave = employeeRepository.save(employee);

            OrganizationEntity organization = new OrganizationEntity();
            organization.setOrganizationName("Office");
            organization.setOrganizationCity("");
            organization.setStatus(Status.ENABLED);

            HashMap<LessonType, SettingLessonsTime> settings = new HashMap<>();
            settings.put(LessonType.INDIVIDUAL, new SettingLessonsTime(
                    LocalTime.of(9, 0),
                    LocalTime.of(21, 0),
                    60L,
                    15L,
                    organization
            ));
            organization.setSettingLessons(settings);
            OrganizationEntity organizationSave = organizationRepository.save(organization);

            ContractEmployeeEntity contract = new ContractEmployeeEntity();
            ServiceTypeEntity serviceType = serviceTypeRepository.save(new ServiceTypeEntity("MANAGE", "Адміністрування", Status.ENABLED));

            SalaryEmployeeEntity salary = new SalaryEmployeeEntity();
            salary.setValue(0L);
            salary.setServiceType(serviceType);
            salary.setContractEmployee(contract);
            salary.setLessonType(LessonType.INDIVIDUAL);

            contract.setEmployee(employeeSave);
            contract.setOrganization(organizationSave);
            contract.setRole(RoleUser.SUPER_ADMIN);
            contract.setStatus(Status.ENABLED);
            contract.setSalary(Set.of(salary));
            contractEmployeeRepository.save(contract);
            System.out.println("-->> адміна було створено");
        }
    }
}

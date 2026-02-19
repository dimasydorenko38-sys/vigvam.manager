package com.sydorenko.vigvam.manager.service.usersServices;

import com.sydorenko.vigvam.manager.dto.request.CreateSalaryEmployeeRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.SalaryEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final ServiceTypeRepository serviceTypeRepository;


    public List<SalaryEmployeeEntity> createSalaryForContract
            (@NotEmpty List<CreateSalaryEmployeeRequestDto> salaryDto, ContractEmployeeEntity contractEmployee) {
        Collection<Long> serviceIds = salaryDto
                .stream()
                .map(CreateSalaryEmployeeRequestDto::getServiceTypeId)
                .collect(Collectors.toSet());
        if (serviceIds.isEmpty()) {
            throw new IllegalArgumentException("Необхідно вказати щонайменше один тип послуги для ЗП");
        }
        Long countServiceTypes = serviceTypeRepository.countByStatusAndIdIn(Status.ENABLED, serviceIds);
        if (serviceIds.size() != countServiceTypes) {
            throw new IllegalArgumentException("Деякі види послуг не активні або не існують");
        }

        return salaryDto
                .stream()
                .map(salary -> SalaryEmployeeEntity.builder()
                            .contractEmployee(contractEmployee)
                            .value(salary.getValue())
                            .premiumValue(salary.getPremiumValue())
                            .serviceType(serviceTypeRepository.getReferenceById(salary.getServiceTypeId()))
                            .lessonType(LessonType.fromString(salary.getLessonType()))
                            .build()
                ).toList();
    }
}

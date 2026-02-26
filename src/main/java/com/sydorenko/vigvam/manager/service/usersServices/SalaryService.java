package com.sydorenko.vigvam.manager.service.usersServices;

import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.CreateSalaryEmployeeRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.SalaryEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.SalaryRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryService extends StatusableService<SalaryEmployeeEntity> {

    private final ServiceTypeRepository serviceTypeRepository;
    private final SalaryRepository salaryRepository;

    public List<SalaryEmployeeEntity> createSalaryForContract
            (@NotEmpty List<CreateSalaryEmployeeRequestDto> salaryDto, ContractEmployeeEntity contractEmployee, LocalDateTime activatedDate) {
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

        Long countSalaryAfterDate = salaryRepository.countByStatusAndContractEmployeeIdAndServiceTypeIdInAndActivatedDateAfter(Status.ENABLED, contractEmployee.getId(), serviceIds, activatedDate);

        return salaryDto
                .stream()
                .map(salary -> SalaryEmployeeEntity.builder()
                        .activatedDate(checkDateSalary(activatedDate, countSalaryAfterDate))
                        .contractEmployee(contractEmployee)
                        .value(salary.getValue())
                        .premiumValue(salary.getPremiumValue())
                        .serviceType(serviceTypeRepository.getReferenceById(salary.getServiceTypeId()))
                        .lessonType(LessonType.fromString(salary.getLessonType()))
                        .status(Status.ENABLED)
                        .build()
                ).toList();
    }

    private LocalDateTime checkDateSalary(LocalDateTime dtoDate, Long countSalaryAfterDate) {
        LocalDateTime activatedDate = dtoDate;
        if (activatedDate == null) {
            activatedDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        } else if (countSalaryAfterDate > 0) {
            throw new IllegalArgumentException("Неможливо обрати цю дату, оскільки зафіксовано зарплати датою пізніше, це призведе до перерахунків вже проведених занять. Якщо ж це необхідно зробити, деактивуйте актуальний запис про зарплату і повторіть редагування контракту");
        }
        return activatedDate;
    }

    public void setDisableStatus(UpdateStatusObjectByIdRequestDto dto) {
        SalaryEmployeeEntity salaryEmployee = salaryRepository.findActiveById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Цей запис деактивовано або не існує в системі"));
        Long contractID = salaryEmployee.getContractEmployee().getId();
        Long serviceTypeId = salaryEmployee.getServiceType().getId();
        LessonType lessonType = salaryEmployee.getLessonType();
        List<SalaryEmployeeEntity> salaryAnalogList = salaryRepository
                .findAllActiveAnalogsAndSortByActivatedDate(
                        Status.ENABLED, contractID, serviceTypeId, lessonType);
        if (salaryAnalogList.size() < 2) {
            throw new IllegalArgumentException("Ви не можете вимкнути єдиний запис ЗП");
        }
        if (salaryAnalogList.getFirst().getId().equals(dto.getId())) {
            salaryEmployee.setStatus(Status.DISABLED);
            salaryRepository.save(salaryEmployee);
        } else throw new IllegalArgumentException("Деактивувати можливо лише останній запис цієї категорії зарплат");
    }

    public void setEnableStatus(UpdateStatusObjectByIdRequestDto dto) {
        super.setEnableStatus(dto.getId(), salaryRepository);
    }
}

package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.dto.request.CreateLessonRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ContractEmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.LessonRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final ContractEmployeeRepository contractEmployeeRepository;
    private final LessonRepository lessonRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final CheckerLesson checkerLesson;

    public void createGenericLesson(@NonNull CreateLessonRequestDto dto) {
        OrganizationEntity organizationLesson = organizationRepository.findById(dto.getOrganization().getId())
                .orElseThrow(() -> new EntityNotFoundException("Організацію не знайдено"));
        ServiceTypeEntity serviceTypeLesson = serviceTypeRepository.findById(dto.getServiceType().getId())
                .orElseThrow(() -> new EntityNotFoundException("Такої послуги не існує в системі"));

        List<ContractEmployeeEntity> contracts = contractEmployeeRepository.findAllByEmployeeId(dto.getEmployee().getId());
        if (contracts.isEmpty()) {
            throw new EntityNotFoundException("Не знайдено спеціаліста");
        }

        EmployeeEntity employee = contracts.stream()
                .map(ContractEmployeeEntity::getEmployee)
                .filter(e -> e.getStatus().equals(Status.ENABLED))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Спеціаліста звільнено або тимчасово вимкнено в системі"));

        contracts.stream()
                .filter(c -> Objects.equals(organizationLesson, c.getOrganization()))
                .flatMap(c -> c.getSalary().stream())
                .filter(s -> Objects.equals(serviceTypeLesson, s.getServiceType())
                        && Objects.equals(s.getLessonType(), LessonType.valueOf(dto.getLessonType())))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Спеціаліст не надає цю послугу у вказаній організації"));

        LessonEntity lesson = LessonEntity.builder()
                .category(LessonCategory.WINDOW)
                .lessonStatus(LessonStatus.valueOf(dto.getLessonStatus()))
                .lessonDateTime(dto.getLessonDateTime())
                .lessonEndTime(dto.getLessonEndTime())
                .type(LessonType.valueOf(dto.getLessonType()))
                .comments(dto.getComments())
                .serviceType(dto.getServiceType())
                .child(dto.getChild())
                .employee(employee)
                .organization(organizationLesson)
                .build();

        lesson = checkerLesson.check(lesson);
        lessonRepository.save(lesson);
    }
}

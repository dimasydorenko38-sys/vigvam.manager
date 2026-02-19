package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.configuration.BusinessConfig;
import com.sydorenko.vigvam.manager.dto.request.CreateLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.*;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.organizationsServices.ServiceTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final ContractEmployeeRepository contractEmployeeRepository;
    private final LessonRepository lessonRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceTypeService serviceTypeService;
    private final CheckerLesson checkerLesson;
    private final BusinessConfig businessConfig;
    private final ChildRepository childRepository;

    public void createGenericLesson(@NonNull CreateLessonRequestDto dto) {
        OrganizationEntity organizationLesson = organizationRepository.findActiveByIdWithSettings(dto.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Організацію не знайдено"));

        ServiceTypeEntity serviceTypeLesson = serviceTypeService.getServiceTypeAndCheck(dto.getServiceTypeId());

        List<ContractEmployeeEntity> contracts = contractEmployeeRepository.findAllActiveWithDetailsByEmployeeId(dto.getEmployeeId());
        if (contracts.isEmpty()) {
            throw new EntityNotFoundException("Не знайдено спеціаліста");
        }

        EmployeeEntity employee = contracts.stream()
                .map(ContractEmployeeEntity::getEmployee)
                .filter(e -> e.getStatus().equals(Status.ENABLED))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Спеціаліста звільнено або тимчасово вимкнено в системі"));

        @NonNull CreateLessonRequestDto finalDto = dto;
        contracts.stream()
                .filter(c -> Objects.equals(organizationLesson.getId(), c.getOrganization().getId()))
                .flatMap(c -> c.getSalary().stream())
                .filter(s -> Objects.equals(serviceTypeLesson.getId(), s.getServiceType().getId())
                        && Objects.equals(s.getLessonType(), LessonType.valueOf(finalDto.getLessonType())))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Спеціаліст не надає цю послугу, або не працює у вказаній організації"));

        dto = checkerLesson.checkStausDto(dto);

        LessonEntity lesson = LessonEntity.builder()
                .category(LessonCategory.WINDOW)
                .lessonStatus(LessonStatus.valueOf(dto.getLessonStatus()))
                .lessonDateTime(dto.getLessonDateTime())
                .lessonEndTime(dto.getLessonEndTime())
                .type(LessonType.valueOf(dto.getLessonType()))
                .comments(dto.getComments())
                .serviceType(serviceTypeLesson)
                .child(childRepository.getReferenceById(dto.getChildId()))
                .employee(employee)
                .organization(organizationLesson)
                .build();

        lesson = checkerLesson.check(lesson);
        lessonRepository.save(lesson);
    }

    public List<LessonResponseProjection> getLessonsByOrgIdForPeriod(Long organizationId, LocalDateTime startDate, LocalDateTime endDate) {
        return lessonRepository.findAllByOrgIdForPeriod(organizationId ,startDate, endDate, businessConfig.getIgnoreLessonStatusesInSchedule());
    }
}

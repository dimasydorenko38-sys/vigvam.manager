package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.configuration.BusinessConfig;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.PlanningLessonResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.AbstractLessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.NotebookPlanScheduleEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.PriceOrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.*;
import com.sydorenko.vigvam.manager.service.organizationsServices.ServiceTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckerLessonService {

    private final ChildRepository childRepository;
    private final LessonRepository lessonRepository;
    private final BusinessConfig businessConfig;
    private final ClientsOrganizationsRepository clientsOrganizationsRepository;
    private final ContractEmployeeRepository contractEmployeeRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceTypeService serviceTypeService;
    private final PlanningLessonRepository planningLessonRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public LessonEntity check(@NonNull LessonEntity lesson) {
        lesson = validationChild(lesson);
        lesson = checkLessonTime(lesson);
        if (checkOverlayOfLessons(lesson)) throw new DuplicateRequestException("Урок на цей час уже існує");
        return lesson;
    }

    public boolean isLessonTypeConflict(LessonType lessonType1, LessonType lessonType2) {
        List<LessonType> lessonTypeList = businessConfig.getTypesForCheckOfOverlayOfLessons(lessonType1);
        return lessonTypeList.contains(lessonType2);
    }

    public boolean canCheckByDate(LocalDateTime startLesson, LocalDateTime endTimeLesson, Long organizationId) {
        boolean needCheck = startLesson.toLocalDate().isAfter(LocalDate.now().plusDays(1));
        boolean canCheck = !lessonRepository.existsCategoriesLessonsThisDay(
                organizationId,
                startLesson.toLocalDate().atStartOfDay(),
                endTimeLesson.toLocalDate().atTime(LocalTime.MAX),
                LessonCategory.REGULAR, businessConfig.getCanIgnoreStatusesInOverlayLessons()
        );
        if (needCheck) {
            canCheck = true;
        }
        return canCheck;
    }

    public boolean checkOverlayOfFactVsPlan(LessonEntity lesson) {
        return planningLessonRepository.existsOverlayOfLessons(lesson.getOrganization().getId(), lesson.getEmployee().getId(),
                businessConfig.getTypesForCheckOfOverlayOfLessons(lesson.getLessonType()), lesson.getStartTimeForChecking(),
                lesson.getEndTimeForChecking(), lesson.getLessonDateTime().getDayOfWeek(),
                businessConfig.getIgnoreLessonStatusesInSchedule(), null, lesson.getParentPlanId());
    }

    public boolean existsNotebookPlanForLessonList(PlanningLessonResponseDto planLesson, LocalDate date, List<NotebookPlanScheduleEntity> notebookPlanList) {
        LocalDateTime planStartDate = date.atTime(planLesson.getLessonTime());
        LocalDateTime planEndDate = date.atTime(planLesson.getLessonEndTime());

        for (var note : notebookPlanList) {
            boolean existTimeInNote = !note.getStartDate().isAfter(planStartDate) && !planEndDate.isAfter(note.getEndDate());
            boolean existChildIdInNote = note.getChild().getId().equals(planLesson.getChildId());
            if (existTimeInNote && existChildIdInNote) {
                boolean existLessonTypeInNote = note.getLessonTypeList().isEmpty() || note.getLessonTypeList().contains(planLesson.getLessonType());
                boolean existServiceTypeInNote = note.getServiceTypeIds().isEmpty() || note.getServiceTypeIds().contains(planLesson.getServiceTypeId());
                if (existLessonTypeInNote && existServiceTypeInNote) return true;
            }
        }
        return false;
    }

    public boolean checkOverlayByTimeLessons(LessonResponseProjection fact, PlanningLessonResponseDto plan) {
        if (!fact.getLessonType().equals(plan.getLessonType())) {
            return false;
        }
        LocalTime factStart = fact.getLessonDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        LocalTime factEnd = fact.getLessonEndTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        LocalTime planStart = plan.getLessonTime().truncatedTo(ChronoUnit.MINUTES);
        LocalTime planEnd = plan.getLessonEndTime().truncatedTo(ChronoUnit.MINUTES);
        return factStart.isBefore(planEnd) && factEnd.isAfter(planStart);
    }

    public LessonStatus checkStatus(String strStatus, LocalDateTime LessonDateTime) {
        LocalDate lessonDate = LessonDateTime.toLocalDate();
        boolean isPastDate = lessonDate.isBefore(LocalDate.now());
        if (strStatus == null) {
            return isPastDate ? LessonStatus.DONE : LessonStatus.WAIT;
        }

        LessonStatus lessonStatus = LessonStatus.fromString(strStatus);
        if (isPastDate && lessonStatus.equals(LessonStatus.WAIT)) {
            throw new IllegalArgumentException("Цей статус заняття не доступний для поточної дати");
        } else if (!isPastDate && lessonStatus.equals(LessonStatus.DONE)) {
            throw new IllegalArgumentException("Цей статус заняття не доступний для поточної дати ");
        }
        return lessonStatus;
    }


    public <T extends AbstractLessonEntity> T validationChild(@NonNull T lesson) {

        if (!businessConfig.getIndependentOfChildStatuses().contains(lesson.getLessonStatus())
                && lesson.getChild() == null && (lesson.getComments() == null || lesson.getComments().length() < 5)) {
            throw new IllegalArgumentException("Необхідно вказати дитину або коментар (від 5 символів)");
        }

        if (lesson.getChild() != null) {
            ChildEntity childEntity = childRepository.getReferenceActiveById(lesson.getChild().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Така дитина не зареєстрована або вимкнена в системі"));
            Long clientId = childEntity.getClient().getId();
            Long organizationId = lesson.getOrganization().getId();
            if (!clientsOrganizationsRepository.existsByClientIdAndOrganizationIdAndStatus(clientId, organizationId, Status.ENABLED)) {
                throw new IllegalArgumentException("Ця дитина не відвідує заняття у цій організації");
            }
            lesson.setChild(childEntity);
        }
        return lesson;
    }

    public boolean checkOverlayOfLessons(LessonEntity lesson) {
        return lessonRepository.existsOverlayOfLessons(
                lesson.getOrganization().getId(),
                lesson.getEmployee().getId(),
                businessConfig.getTypesForCheckOfOverlayOfLessons(lesson.getLessonType()),
                lesson.getLessonDateTime(),
                lesson.getLessonEndTime(),
                businessConfig.getCanIgnoreStatusesInOverlayLessons(),
                lesson.getId());
    }

    public boolean checkOverlayOfPlaningLessons(PlanningLessonEntity lesson) {
        return planningLessonRepository.existsOverlayOfLessons(
                lesson.getOrganization().getId(),
                lesson.getEmployee().getId(),
                businessConfig.getTypesForCheckOfOverlayOfLessons(lesson.getLessonType()),
                lesson.getLessonTime(),
                lesson.getLessonEndTime(),
                lesson.getLessonDayOfWeek(),
                businessConfig.getCanIgnoreStatusesInOverlayLessons(),
                lesson.getId(),null);
    }

    public <T extends AbstractLessonEntity> T checkLessonTime(@NonNull T lesson) {
        LocalTime lessonTime = lesson.getStartTimeForChecking();
        LocalTime lessonEndTime = lesson.getEndTimeForChecking();
        SettingLessonsTime settingLessonsTime = lesson.getOrganization().getSettingLessons().get(lesson.getLessonType());
        if (lessonEndTime != null) {
            long durationLesson = Duration.between(lessonTime, lessonEndTime)
                    .toMinutes();
            if (durationLesson < 0) {
                throw new IllegalArgumentException("Заняття не може починатися після його закінчення");
            } else if (durationLesson > 360) {
                throw new IllegalArgumentException("Заняття не може тривати так довго");
            } else {
                lesson.setEndTimeForChecking(lessonEndTime);
            }
        } else {
            lessonEndTime = lessonTime
                    .plusMinutes(settingLessonsTime.getLessonDurationMinutes())
                    .truncatedTo(ChronoUnit.MINUTES);
            lesson.setEndTimeForChecking(lessonEndTime);
        }
        if (settingLessonsTime.getFirstHourOfWork().isAfter(lessonTime)
                || settingLessonsTime.getLastHourOfWork().isBefore(lessonEndTime)) {
            throw new IllegalArgumentException("Час уроку виходить за межі робочого графіку організації");
        }
        return lesson;
    }

    public EntitiesForLesson getEntitiesForLesson(Long serviceTypeId, Long employeeId, Long organizationId, String lessonType, Long childId) {
        OrganizationEntity organizationLesson = organizationRepository.findActiveByIdWithSettingsAndPrice(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Організацію не знайдено"));

        ServiceTypeEntity serviceTypeLesson = serviceTypeService.getServiceTypeAndCheck(serviceTypeId);

        List<PriceOrganizationEntity> priceList = organizationLesson.getPrice();
        priceList.stream().filter(price -> serviceTypeLesson.getId().equals(price.getServiceType().getId()))
                .filter(price -> price.getStatus().equals(Status.ENABLED))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Прайс організації не містить такої послуги"));

        List<ContractEmployeeEntity> contracts = contractEmployeeRepository.findAllActiveWithDetailsByEmployeeId(employeeId);
        if (contracts.isEmpty()) {
            throw new EntityNotFoundException("Не знайдено спеціаліста");
        }

        EmployeeEntity employee = contracts.stream()
                .map(ContractEmployeeEntity::getEmployee)
                .filter(e -> e.getStatus().equals(Status.ENABLED))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Спеціаліста звільнено або тимчасово вимкнено в системі"));

        contracts.stream()
                .filter(c -> Objects.equals(organizationLesson.getId(), c.getOrganization().getId()))
                .flatMap(c -> c.getSalary().stream())
                .filter(s -> Objects.equals(serviceTypeLesson.getId(), s.getServiceType().getId())
                        && Objects.equals(s.getLessonType(), LessonType.fromString(lessonType)))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Спеціаліст не надає цю послугу, або не працює у вказаній організації"));

        ChildEntity child = null;
        if (childId != null) {
            child = childRepository.getReferenceById(childId);
        }
        return EntitiesForLesson.builder()
                .organization(organizationLesson)
                .serviceType(serviceTypeLesson)
                .employee(employee)
                .child(child)
                .build();
    }

    public void checkEntityForLessonLists(Set<EmployeeEntity> employeeList ,Set<ChildEntity> childEntityList, Set<ServiceTypeEntity> serviceTypeEntityList, Long organizationId) {
    Set<Long> childIds = childEntityList.stream().filter(Objects::nonNull).map(ChildEntity::getId).filter(Objects::nonNull).collect(Collectors.toSet());
    Set<Long> serviceTypeIds = serviceTypeEntityList.stream().filter(Objects::nonNull).map(ServiceTypeEntity::getId).collect(Collectors.toSet());
    Set<Long> employeeIds = employeeList.stream().filter(Objects::nonNull).map(EmployeeEntity::getId).collect(Collectors.toSet());

    long countContractsEmployeeInDB = contractEmployeeRepository.countByEmployeeIdInAndOrganizationIdAndRoleAndStatus(employeeIds,organizationId, RoleUser.EMPLOYEE, Status.ENABLED);
    if (countContractsEmployeeInDB != employeeIds.size()) throw new EntityNotFoundException("В плановому графіку існують уроки з неактивним викладачем, неможливо створити такий фактичний урок") ;

    long childrenCountInDB = childRepository.countByStatusAndIdIn(Status.ENABLED, childIds);
    if (childrenCountInDB != childIds.size()) throw new EntityNotFoundException("Ймовірно якась із дітей у списку уроків вже не активна, необхідно перевірити Плановий графік");

    long serviceCountInDB = serviceTypeRepository.countByStatusAndIdIn(Status.ENABLED, serviceTypeIds);
    if(serviceCountInDB!= serviceTypeIds.size()) throw new EntityNotFoundException("Одна з послуг вже не активна в системі, необхідно перевірии Планові заняття");
    }
}

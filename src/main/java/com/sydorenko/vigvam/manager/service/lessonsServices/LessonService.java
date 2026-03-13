package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.configuration.BusinessConfig;
import com.sydorenko.vigvam.manager.dto.request.lessons.CreateLessonByPlanLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.request.lessons.UpdateLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.request.lessons.CreateLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.response.GenerateLessonListByPlanListResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.PlanningLessonResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.NotebookPlanScheduleEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.*;
import com.sydorenko.vigvam.manager.service.GenericService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CheckerLessonService checkerLessonService;
    private final BusinessConfig businessConfig;
    private final PlanningLessonRepository planningLessonRepository;
    private final GenericService genericService;
    private final OrganizationRepository organizationRepository;
    private final NotebookPlanRepository notebookPlanRepository;


    public void createGenericLesson(@NonNull CreateLessonRequestDto dto) {
        LessonStatus lessonStatus = checkerLessonService.checkStatus(dto.getLessonStatus(), dto.getLessonDateTime());
        EntitiesForLesson entitiesForLesson = checkerLessonService.getEntitiesForLesson(dto.getServiceTypeId(), dto.getEmployeeId(), dto.getOrganizationId(), dto.getLessonType(), dto.getChildId());
        LessonEntity lesson = getLessonEntity(dto, lessonStatus, entitiesForLesson);
        lesson = checkerLessonService.check(lesson);
        if (checkerLessonService.checkOverlayOfFactVsPlan(lesson)
                && !businessConfig.getIgnoreLessonStatusesInSchedule().contains(lesson.getLessonStatus()))
            throw new DuplicateRequestException("Існує плановий урок на цей час");
        lessonRepository.save(lesson);
    }

    private LessonEntity getLessonEntity(@NonNull CreateLessonRequestDto dto, LessonStatus lessonStatus, EntitiesForLesson entitiesForLesson) {
        LessonCategory lessonCategory = dto.getLessonCategory();
        if (lessonCategory != LessonCategory.REGULAR) lessonCategory = LessonCategory.WINDOW;
        return LessonEntity.builder()
                .category(lessonCategory)
                .parentPlanId(dto.getParentPlanId())
                .lessonStatus(lessonStatus)
                .lessonDateTime(dto.getLessonDateTime())
                .lessonEndTime(dto.getLessonEndTime())
                .lessonType(LessonType.fromString(dto.getLessonType()))
                .comments(dto.getComments())
                .serviceType(entitiesForLesson.getServiceType())
                .child(entitiesForLesson.getChild())
                .employee(entitiesForLesson.getEmployee())
                .organization(entitiesForLesson.getOrganization())
                .build();
    }

    public void createLessonByPlanLesson(CreateLessonByPlanLessonRequestDto dto) {
        PlanningLessonResponseDto planLesson = new PlanningLessonResponseDto(planningLessonRepository.findById(dto.planLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Не знайдено такого уроку")));
        if (dto.date().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Неможливо перевести план у факт заднім числом. Змініть дату");
        genericService.checkAuditorByOrganization(planLesson.getOrganizationId());
        createGenericLesson(new CreateLessonRequestDto(planLesson, dto.date()));
    }

    public void createLessonListByPlanList(GenerateLessonListByPlanListResponseDto dto) {

        if (dto.date().isAfter(LocalDate.now().plusDays(1))) throw new IllegalArgumentException("Згенерувати автоматично пожна до : " + LocalDate.now().plusDays(1) + " (включно). Генерація уроків з плану вимикає перевірку накладок на цю дату!");

        genericService.checkAuditorByOrganization(dto.organizationId());
        List<NotebookPlanScheduleEntity> noteList = notebookPlanRepository.findAllByOrgIdAndPeriodAndStatus(dto.organizationId(), dto.date().atTime(LocalTime.MAX), dto.date().atStartOfDay(), Status.ENABLED);
        OrganizationEntity organization = organizationRepository.findActiveById(dto.organizationId())
                .orElseThrow(() -> new EntityNotFoundException("Організацію не знайдено"));

        List<PlanningLessonEntity> planningLessonsList;
        if (!dto.planLessonIds().isEmpty()) {
            planningLessonsList = planningLessonRepository.findAllByIdIn(dto.planLessonIds());
            if (planningLessonsList.size() != dto.planLessonIds().size())
                throw new EntityNotFoundException("Не вдалося знайти всі роки зі списку");
        } else {
            DayOfWeek dayOfWeek = dto.date().getDayOfWeek();
            planningLessonsList = planningLessonRepository.findAllByLessonDayOfWeekAndOrganizationIdAndLessonStatusNotIn(dayOfWeek, dto.organizationId(), businessConfig.getIgnoreLessonStatusesInSchedule());
        }
        if (planningLessonsList.isEmpty()) throw new EntityNotFoundException("Плановий графік порожній");
        List<PlanningLessonResponseDto> planningLessonResponseDtoList = planningLessonsList.stream()
                .map(PlanningLessonResponseDto::new)
                .filter(plan -> !checkerLessonService.existsNotebookPlanForLessonList(plan, dto.date(), noteList))
                .toList();

        List<PlanningLessonResponseDto> planLessonThisNoteDtoList = planningLessonsList.stream()
                .map(PlanningLessonResponseDto::new)
                .filter(plan -> checkerLessonService.existsNotebookPlanForLessonList(plan, dto.date(), noteList))
                .toList();

        Map<Long, EmployeeEntity> employeeEntityMap = planningLessonsList.stream().map(PlanningLessonEntity::getEmployee).collect(Collectors.toMap(EmployeeEntity::getId, Function.identity(), (existing, replacement) -> existing));
        Map<Long, ChildEntity> childEntityMap = planningLessonsList.stream().map(PlanningLessonEntity::getChild).filter(Objects::nonNull).collect(Collectors.toMap(ChildEntity::getId, Function.identity(), (existing, replacement) -> existing));
        Map<Long, ServiceTypeEntity> serviceTypeEntityMap = planningLessonsList.stream().map(PlanningLessonEntity::getServiceType).collect(Collectors.toMap(ServiceTypeEntity::getId, Function.identity(), (existing, replacement) -> existing));

        checkerLessonService.checkEntityForLessonLists(new HashSet<>(employeeEntityMap.values()), new HashSet<>(childEntityMap.values()), new HashSet<>(serviceTypeEntityMap.values()), organization.getId());

        List<LessonEntity> lessonEntityList = planningLessonResponseDtoList.stream()
                .map(plan ->
                        getLessonEntity(new CreateLessonRequestDto(
                                        plan, dto.date()),
                                LessonStatus.WAIT,
                                new EntitiesForLesson(plan, organization, employeeEntityMap, childEntityMap, serviceTypeEntityMap)))
                .toList();

        List<LessonEntity> canceledLessonEntityList = planLessonThisNoteDtoList.stream()
                .map(plan ->
                        getLessonEntity(new CreateLessonRequestDto(
                                        plan, dto.date(), "Скасовано через записку"),
                                LessonStatus.MISSED_FREE,
                                new EntitiesForLesson(plan, organization, employeeEntityMap, childEntityMap, serviceTypeEntityMap)))
                .toList();

        List<LessonEntity> lessonFactList = lessonRepository.findAllEntityByOrgIdForPeriod(
                dto.organizationId(), dto.date().atStartOfDay(), dto.date().atTime(LocalTime.MAX),
                businessConfig.getCanIgnoreStatusesInOverlayLessons());
        if (!lessonFactList.isEmpty()) {
            Map<Long, List<LessonEntity>> lessonEntityMap = lessonEntityList.stream().collect(Collectors.groupingBy(l -> l.getEmployee().getId()));
            lessonFactList.forEach(fact -> {
                List<LessonEntity> lessonEntityByEmployee = lessonEntityMap.getOrDefault(fact.getEmployee().getId(), List.of());
                lessonEntityByEmployee.forEach(lessonEntity -> {
                    if (checkerLessonService.isLessonTypeConflict(lessonEntity.getLessonType(), fact.getLessonType())
                            && lessonEntity.getLessonDateTime().isBefore(fact.getLessonEndTime())
                            && lessonEntity.getLessonEndTime().isAfter(fact.getLessonDateTime())) {
                        throw new DuplicateRequestException(
                                String.format("Накладка у викладача з ID: %d, час: %s - перетинається з існуючим уроком ID: %s",
                                        fact.getEmployee().getId(), lessonEntity.getLessonDateTime().toLocalTime(), fact.getId())
                        );
                    }
                });
            });

        }
        lessonRepository.saveAll(canceledLessonEntityList);
        lessonRepository.saveAll(lessonEntityList);
    }

    public void updateLesson(UpdateLessonRequestDto dto) {
        LessonEntity lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Такого уроку не існує. ID: " + dto.getLessonId()));
        LessonStatus lessonStatus = checkerLessonService.checkStatus(dto.getLessonStatus(), dto.getLessonDateTime());

        EntitiesForLesson entitiesForLesson = checkerLessonService.getEntitiesForLesson(dto.getServiceTypeId(), dto.getEmployeeId(), dto.getOrganizationId(), dto.getLessonType(), dto.getChildId());
        if (!lessonStatus.equals(lesson.getLessonStatus())) {
            lesson.setUpdatedLessonStatus(LocalDateTime.now());
        }
        lesson.setLessonStatus(lessonStatus);
        lesson.setLessonDateTime(dto.getLessonDateTime());
        lesson.setLessonEndTime(dto.getLessonEndTime());
        lesson.setLessonType(LessonType.fromString(dto.getLessonType()));
        lesson.setComments(dto.getComments());
        lesson.setServiceType(entitiesForLesson.getServiceType());
        lesson.setChild(entitiesForLesson.getChild());
        lesson.setEmployee(entitiesForLesson.getEmployee());
        lesson.setOrganization(entitiesForLesson.getOrganization());

        lesson = checkerLessonService.check(lesson);
        boolean canCheck = checkerLessonService.canCheckByDate(lesson.getLessonDateTime(), lesson.getLessonEndTime(), lesson.getOrganization().getId());
        if (canCheck
                && !businessConfig.getIgnoreLessonStatusesInSchedule().contains(lesson.getLessonStatus())
                && checkerLessonService.checkOverlayOfFactVsPlan(lesson))
            throw new DuplicateRequestException("Існує плановий урок на цей час");
        lessonRepository.save(lesson);
    }

    public List<LessonResponseProjection> getLessonsByOrgIdForPeriod(Long organizationId, LocalDateTime startDate, LocalDateTime endDate) {
        return lessonRepository.findAllByOrgIdForPeriod(organizationId, startDate, endDate, businessConfig.getIgnoreLessonStatusesInSchedule());
    }
}

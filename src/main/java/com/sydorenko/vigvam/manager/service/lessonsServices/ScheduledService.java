package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.configuration.BusinessConfig;
import com.sydorenko.vigvam.manager.dto.request.lessons.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.*;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.NotebookPlanScheduleEntity;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.repository.NotebookPlanRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.usersServices.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduledService {

    private final OrganizationRepository organizationRepository;
    private final LessonService lessonService;
    private final ContractEmployeeService contractEmployeeService;
    private final EmployeeService employeeService;
    private final PlanningLessonService planningLessonService;
    private final BusinessConfig businessConfig;
    private final NotebookPlanRepository notebookPlanRepository;
    private final CheckerLessonService checkerLessonService;

    public ScheduleResponseDto getScheduleByPeriod(GetScheduleRequestDto dto) {
        LocalDateTime startDate = dto.getStartDate().atStartOfDay();
        LocalDateTime endDate;
        if (dto.getEndDate() == null) {
            endDate = dto.getStartDate().atTime(LocalTime.MAX);
        } else endDate = dto.getEndDate().atTime(LocalTime.MAX);
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Період вказано з помилкою, кінцева дата не може бути меншою за початкову");
        }

        List<LocalDate> period = new ArrayList<>();
        for (LocalDate i = startDate.toLocalDate(); !i.isAfter(endDate.toLocalDate()); i = i.plusDays(1)) {
            period.add(i);
        }

        OrganizationEntity organization = organizationRepository.findActiveByIdWithSettings(dto.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Не знайдено цієї організації"));

        Set<EmployeeNameResponseProjection> employees = new HashSet<>(contractEmployeeService.getAllEmployeeNamesByOrg(dto.getOrganizationId()));
        Set<Long> employeeActiveIds = employees.stream().map(EmployeeNameResponseProjection::getId).collect(Collectors.toSet());

        List<LessonResponseProjection> lessons = lessonService
                .getLessonsByOrgIdForPeriod(dto.getOrganizationId(), startDate, endDate);

        Set<DayOfWeek> dayOfWeekSet = period
                .stream()
                .map(LocalDate::getDayOfWeek)
                .collect(Collectors.toSet());

        List<NotebookPlanScheduleEntity> notebookPlanList = notebookPlanRepository
                .findAllByOrgIdAndPeriodAndStatus(organization.getId(), endDate, startDate, Status.ENABLED);

        List<PlanningLessonEntity> planningLessonEntityList = planningLessonService
                .getLessonsByOrgIdForPeriod(dto.getOrganizationId(), dayOfWeekSet);

        Map<DayOfWeek, List<PlanningLessonResponseDto>> planningLessons = planningLessonEntityList.stream()
                .map(PlanningLessonResponseDto::new)
                .collect(Collectors.groupingBy(PlanningLessonResponseDto::getLessonDayOfWeek, Collectors.toList()));

        Map<LocalDate, List<LessonResponseProjection>> lessonsByDate = lessons.stream()
                .collect(Collectors.groupingBy(lesson -> lesson.getLessonDateTime().toLocalDate(), Collectors.toList()));

        Map<LocalDate, DayScheduleResponseDto> schedule = period.stream()
                .collect(Collectors.toMap(
                        date -> date,
                        date -> createSomeDaySchedule(
                                date,
                                planningLessons.getOrDefault(date.getDayOfWeek(), List.of()).stream()
                                        .filter(planningLesson -> !checkerLessonService.existsNotebookPlanForLessonList(planningLesson, date, notebookPlanList))
                                        .toList(),
                                lessonsByDate.getOrDefault(date, List.of()),
                                employees)));

        Set<Long> employeeIdsByAllLessons = lessons.stream().map(LessonResponseProjection::getEmployeeId).collect(Collectors.toSet());
        Set<Long> employeeIdsByPlanLessons = planningLessons.values().stream().flatMap(List::stream)
                .map(PlanningLessonResponseDto::getEmployeeId).collect(Collectors.toSet());
        employeeIdsByAllLessons.addAll(employeeIdsByPlanLessons);
        if (!employeeActiveIds.equals(employeeIdsByAllLessons)) {
            employeeActiveIds.addAll(employeeIdsByAllLessons);
        }
        Set<EmployeeNameResponseProjection> employeeNameByAllLessons = employeeService.getAllEmployeesByIds(employeeActiveIds);

        return ScheduleResponseDto.builder()
                .organization(new OrganizationResponseDto(organization))
                .employeesData(employeeNameByAllLessons)
                .schedule(schedule)
                .build();
    }

    private DayScheduleResponseDto createSomeDaySchedule(LocalDate date, List<PlanningLessonResponseDto> planningLessons,
                                                         List<LessonResponseProjection> lessons,
                                                         Set<EmployeeNameResponseProjection> employees) {
        var employeeIds = employees.stream()
                .map(EmployeeNameResponseProjection::getId).collect(Collectors.toSet());
        var employeeIdsByLessons = lessons.stream()
                .map(LessonResponseProjection::getEmployeeId)
                .collect(Collectors.toSet());
        var employeeIdsByPlan = planningLessons.stream()
                .map(PlanningLessonResponseDto::getEmployeeId)
                .collect(Collectors.toSet());

        Set<Long> allEmployeeIds = new HashSet<>(employeeIds);
        allEmployeeIds.addAll(employeeIdsByPlan);
        allEmployeeIds.addAll(employeeIdsByLessons);

        List<ConflictResponseDto> conflictList = List.of();
        if (date.isBefore(LocalDate.now())) {
            return new DayScheduleResponseDto(employeeIdsByLessons, lessons, List.of(), conflictList);
        } else if (date.equals(LocalDate.now())) {
            return new DayScheduleResponseDto(allEmployeeIds, lessons, List.of(), conflictList);
        } else {
            if (!lessons.isEmpty()) {
                LessonResponseProjection lesson = lessons.getFirst();
                boolean canCheck = checkerLessonService.canCheckByDate(lesson.getLessonDateTime(), lesson.getLessonEndTime(), lesson.getOrganizationId());
                if (canCheck) {
                    conflictList = getConflictList(planningLessons, lessons);
                }
            }
            return new DayScheduleResponseDto(allEmployeeIds, lessons, planningLessons, conflictList);
        }
    }

    private List<ConflictResponseDto> getConflictList(List<PlanningLessonResponseDto> planningLessons, List<LessonResponseProjection> lessons) {
        return lessons.stream()
                .map(fact -> planningLessons.stream()
                        .filter(plan -> businessConfig.getTypesForCheckOfOverlayOfLessons(plan.getLessonType()).contains(fact.getLessonType()))
                        .filter(plan -> plan.getEmployeeId().equals(fact.getEmployeeId()))
                        .filter(plan -> checkerLessonService.checkOverlayByTimeLessons(fact, plan))
                        .findFirst()
                        .map(plan -> new ConflictResponseDto(
                                fact.getLessonDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES),
                                fact.getEmployeeId(),
                                fact.getId(),
                                plan.getId(),
                                fact.getLessonType(),
                                "Конфлікт з плановим графіком. Необхідно поставити пропуск на план або відмінити поточне заняття")
                        )
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }
}

package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.dto.request.lessons.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.*;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.usersServices.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanScheduledService {

    private final OrganizationRepository organizationRepository;
    private final ContractEmployeeService contractEmployeeService;
    private final EmployeeService employeeService;
    private final PlanningLessonService planningLessonService;


    public PlanScheduleResponseDto getPlanScheduleByPeriod(GetScheduleRequestDto dto) {

        OrganizationEntity organization = organizationRepository.findActiveByIdWithSettings(dto.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Не знайдено цієї організації"));

        Set<EmployeeNameResponseProjection> employees = new HashSet<>(contractEmployeeService.getAllEmployeeNamesByOrg(dto.getOrganizationId()));

        Set<DayOfWeek> dayOfWeekSet = Arrays.stream(DayOfWeek.values()).collect(Collectors.toSet());

        List<PlanningLessonEntity> lessons = planningLessonService
                .getLessonsByOrgIdForPeriod(dto.getOrganizationId(), dayOfWeekSet);

        Map<DayOfWeek, List<PlanningLessonResponseDto>> lessonsByDate = lessons.stream()
                .map(PlanningLessonResponseDto::new)
                .collect(Collectors.groupingBy(PlanningLessonResponseDto::getLessonDayOfWeek, Collectors.toList()));

        Map<DayOfWeek, PlanDayScheduleResponseDto> schedule = dayOfWeekSet.stream()
                .collect(Collectors.toMap(
                        day -> day,
                        day -> createSomeDaySchedule(
                                day,
                                lessonsByDate.getOrDefault(day, List.of()),
                                employees)));

        Set<Long> employeeIdsActive = employees.stream().map(EmployeeNameResponseProjection::getId).collect(Collectors.toSet());
        Set<Long> employeeIdsByAllLessons = lessons.stream().map(planningLesson -> planningLesson.getEmployee().getId()).collect(Collectors.toSet());
        employeeIdsByAllLessons.addAll(employeeIdsActive);
        if (!employeeIdsActive.containsAll(employeeIdsByAllLessons)) {
            Set<EmployeeNameResponseProjection> employeeNameByAllLessons = employeeService.getAllEmployeesByIds(employeeIdsByAllLessons);
            employees.addAll(employeeNameByAllLessons);
        }

        return PlanScheduleResponseDto.builder()
                .organization(new OrganizationResponseDto(organization))
                .employeesData(employees)
                .schedule(schedule)
                .build();
    }
    private PlanDayScheduleResponseDto createSomeDaySchedule(DayOfWeek day,
                                                         List<PlanningLessonResponseDto> lessons,
                                                         Set<EmployeeNameResponseProjection> employees) {
        var employeeIds = employees.stream()
                .map(EmployeeNameResponseProjection::getId).collect(Collectors.toSet());
        var employeeIdsByLessons = lessons.stream()
                .map(PlanningLessonResponseDto::getEmployeeId)
                .collect(Collectors.toSet());

        Set<Long> allEmployeeIds = new HashSet<>(employeeIds);
        allEmployeeIds.addAll(employeeIdsByLessons);

        return new PlanDayScheduleResponseDto(allEmployeeIds, lessons);
    }
}

package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.*;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.lessonsServices.LessonService;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import com.sydorenko.vigvam.manager.service.usersServices.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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


    public ScheduleResponseDto getScheduleByPeriod(GetScheduleRequestDto dto) {
        LocalDateTime startDate = dto.getStartDate().atStartOfDay();
        LocalDateTime endDate;
        if (dto.getEndDate() == null) {
            endDate = dto.getStartDate().atTime(LocalTime.MAX);
        } else endDate = dto.getEndDate().atTime(LocalTime.MAX);
        if(endDate.isBefore(startDate)){
            throw new IllegalArgumentException("Період вказано з помилкою, кінцева дата не може бути меншою за початкову");
        }

        List<LocalDate> period = new ArrayList<>();
        for (LocalDate i = startDate.toLocalDate(); !i.isAfter(endDate.toLocalDate()); i = i.plusDays(1)) {
            period.add(i);
        }

        OrganizationEntity organization = organizationRepository.findActiveByIdWithSettings(dto.getOrganization().getId())
                .orElseThrow(() -> new EntityNotFoundException("Не знайдено цієї організації"));

        Set<EmployeeNameResponseProjection> employees = new HashSet<>(contractEmployeeService.getAllEmployeeNamesByOrg(dto.getOrganization().getId()));

        List<LessonResponseProjection> lessons = lessonService
                .getLessonsByOrgIdForPeriod(dto.getOrganization().getId(), startDate, endDate);

        Map<LocalDate, List<LessonResponseProjection>> lessonsByDate = lessons.stream()
                .collect(Collectors.groupingBy(lesson -> lesson.getLessonDateTime().toLocalDate(), Collectors.toList()));

        Map<LocalDate, DayScheduleResponseDto> schedule = period.stream()
                .collect(Collectors.toMap(
                        date -> date,
                        date -> createSomeDaySchedule(lessonsByDate.getOrDefault(date, List.of()), employees)));

        Set<Long> employeeIdsActive = employees.stream().map(EmployeeNameResponseProjection::getId).collect(Collectors.toSet());
        Set<Long> employeeIdsByAllLessons = lessons.stream().map(LessonResponseProjection::getEmployee).collect(Collectors.toSet());
        if (!employeeIdsActive.equals(employeeIdsByAllLessons)) {
            Set<EmployeeNameResponseProjection> employeeNameByAllLessonsResponseProjections = employeeService.getAllEmployeesByIds(employeeIdsByAllLessons);
            employees.addAll(employeeNameByAllLessonsResponseProjections);
        }


        return ScheduleResponseDto.builder()
                .organization(new OrganizationResponseDto(organization))
                .employeesData(employees)
                .schedule(schedule)
                .build();
    }

    public DayScheduleResponseDto createSomeDaySchedule(List<LessonResponseProjection> lessons, Set<EmployeeNameResponseProjection> employees) {
        var employeeIds = employees.stream()
                .map(EmployeeNameResponseProjection::getId).collect(Collectors.toSet());
        var employeeIdsByLessons = lessons.stream()
                .map(LessonResponseProjection::getEmployee)
                .collect(Collectors.toSet());
        Set<Long> combinedIds = new HashSet<>(employeeIds);
        combinedIds.addAll(employeeIdsByLessons);

        if (!lessons.isEmpty()) {
            for (var lesson : lessons) {
                if (lesson.getLessonDateTime().toLocalDate().isBefore(LocalDate.now())) {
                    return new DayScheduleResponseDto(employeeIdsByLessons, lessons);
                } else return new DayScheduleResponseDto(combinedIds, lessons);
            }
        } else return new DayScheduleResponseDto(employeeIds, lessons);
        throw  new IllegalArgumentException("Не вдалося завантажити графік на один із днів періоду");
    }
}

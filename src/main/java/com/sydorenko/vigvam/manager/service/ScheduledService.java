package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.EmployeeNameResponseProjection;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.OrganizationResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ScheduleResponseDto;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.lessonsServices.LessonService;
import com.sydorenko.vigvam.manager.service.usersServices.ContractEmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduledService {

    private final OrganizationRepository organizationRepository;
    private final LessonService lessonService;
    private final ContractEmployeeService contractEmployeeService;

    public ScheduleResponseDto getScheduleByPeriod(GetScheduleRequestDto dto) {
        LocalDateTime startDate = dto.getStartDate().atStartOfDay();
        LocalDateTime endDate;
        if(dto.getEndDate() == null){
            endDate = dto.getStartDate().atTime(LocalTime.MAX);
        }else  endDate = dto.getEndDate().atTime(LocalTime.MAX);

        OrganizationEntity organization = organizationRepository.findByIdWithSettings(dto.getOrganization().getId())
                .orElseThrow(()-> new EntityNotFoundException("Не знайдено цієї організації"));
        Set<EmployeeNameResponseProjection> employees = new HashSet<>(contractEmployeeService.getAllEmployeeNamesByOrg(dto.getOrganization().getId()));

        List<LessonResponseProjection> lessons = lessonService
                .getLessonsByOrgIdForPeriod(dto.getOrganization().getId(),startDate, endDate);

        return ScheduleResponseDto.builder()
                .organization(new OrganizationResponseDto(organization))
                .employees(employees)
                .lessons(lessons)
                .build();
    }
}

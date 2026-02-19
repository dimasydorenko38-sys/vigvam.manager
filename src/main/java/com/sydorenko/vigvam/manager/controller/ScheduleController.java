package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ChildNameResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ScheduleResponseDto;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.ScheduledService;
import com.sydorenko.vigvam.manager.service.usersServices.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*")  // тест візуал для графіку
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduledService scheduledService;
    private final ChildService childService;
    private final GenericService genericService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/period")
    public ResponseEntity<ScheduleResponseDto> getScheduleByPeriod(@RequestBody GetScheduleRequestDto dto){
        genericService.checkAuditorByOrganization(dto.getOrganizationId());
        ScheduleResponseDto scheduleResponseDto =  scheduledService.getScheduleByPeriod(dto);
        return ResponseEntity.ok(scheduleResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/childNames")
    public ResponseEntity<Set<ChildNameResponseDto>> getAllChildName(@RequestBody GetScheduleRequestDto dto){
        genericService.checkAuditorByOrganization(dto.getOrganizationId());
        Set<ChildNameResponseDto> childNameResponseProjection =  childService.getAllChildNameByOrgId(dto.getOrganizationId());
        return ResponseEntity.ok(childNameResponseProjection);
    }
}

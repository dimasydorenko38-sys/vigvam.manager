package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ChildNameResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ScheduleResponseDto;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.ScheduledService;
import com.sydorenko.vigvam.manager.service.usersServices.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.envers.exception.AuditException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Set;

@CrossOrigin(origins = "*")  // тест візуал для графіку
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduledService scheduledService;
    private final ChildService childService;
    private final GenericService genericService;

    @SneakyThrows
    @PostMapping("/period")
    public ResponseEntity<ScheduleResponseDto> getScheduleByPeriod(@RequestBody GetScheduleRequestDto dto){
        if(!genericService.checkAuditorByOrganization(dto.getOrganization().getId())){
            throw new AccessDeniedException("Користувач не має доступу до організації");
        }
        ScheduleResponseDto scheduleResponseDto =  scheduledService.getScheduleByPeriod(dto);
        return ResponseEntity.ok(scheduleResponseDto);
    }

    @SneakyThrows
    @PostMapping("/childNames")
    public ResponseEntity<Set<ChildNameResponseDto>> getAllChildName(@RequestBody GetScheduleRequestDto dto){
        if(!genericService.checkAuditorByOrganization(dto.getOrganization().getId())){
            throw new AccessDeniedException("Користувач не має доступу до організації");
        }
        Set<ChildNameResponseDto> childNameResponseProjection =  childService.getAllChildNameByOrgId(dto.getOrganization().getId());
        return ResponseEntity.ok(childNameResponseProjection);
    }
}

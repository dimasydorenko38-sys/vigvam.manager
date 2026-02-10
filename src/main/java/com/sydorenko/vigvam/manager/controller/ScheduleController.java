package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ChildNameResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ScheduleResponseDto;
import com.sydorenko.vigvam.manager.service.ScheduledService;
import com.sydorenko.vigvam.manager.service.usersServices.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduledService scheduledService;
    private final ChildService childService;

    @PostMapping("/period")
    public ResponseEntity<ScheduleResponseDto> getScheduleByPeriod(@RequestBody GetScheduleRequestDto dto){
        ScheduleResponseDto scheduleResponseDto =  scheduledService.getScheduleByPeriod(dto);
        return ResponseEntity.ok(scheduleResponseDto);
    }

    @PostMapping("/childNames")
    public ResponseEntity<Set<ChildNameResponseDto>> getAllChildName(@RequestBody GetScheduleRequestDto dto){
        Set<ChildNameResponseDto> childNameResponseProjection =  childService.getAllChildNameByOrgId(dto.getOrganization().getId());
        return ResponseEntity.ok(childNameResponseProjection);
    }
}

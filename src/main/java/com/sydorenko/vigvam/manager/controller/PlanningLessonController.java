package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.lessons.GetScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.request.lessons.CreatePlanningLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.PlanScheduleResponseDto;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.lessonsServices.PlanScheduledService;
import com.sydorenko.vigvam.manager.service.lessonsServices.PlanningLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planning_lesson")
public class PlanningLessonController {

    private final PlanningLessonService planningLessonService;
    private final PlanScheduledService planScheduledService;
    private final GenericService genericService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponseDto> createPlanningLesson (@RequestBody CreatePlanningLessonRequestDto dto){
        genericService.checkAuditorByOrganization(dto.organizationId());
        planningLessonService.createPlanningLesson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<MessageResponseDto> updatePlanningLesson (@RequestBody CreatePlanningLessonRequestDto dto){
        genericService.checkAuditorByOrganization(dto.organizationId());
        planningLessonService.updatePlanningLesson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/getPlanSchedule")
    public ResponseEntity<PlanScheduleResponseDto> getPlanSchedule (@RequestBody GetScheduleRequestDto dto){
        genericService.checkAuditorByOrganization(dto.getOrganizationId());
        return ResponseEntity.status(HttpStatus.OK).body(planScheduledService.getPlanScheduleByPeriod(dto));
    }
}

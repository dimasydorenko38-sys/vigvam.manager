package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.lessons.CreateNotebookPlanScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.lessonsServices.NotebookPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/notebook_plan")
public class NotebookPlanScheduleController {
    private final NotebookPlanService notebookPlanService;

    @PostMapping ("/create")
    public ResponseEntity<MessageResponseDto> createNotebookPlan (@Valid @RequestBody CreateNotebookPlanScheduleRequestDto dto){
        notebookPlanService.createNotebookPlanSchedule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("successful"));
    }

    @PostMapping ("/disable")
    public ResponseEntity<MessageResponseDto> disableNotebookPlan (@RequestBody Long id){
        notebookPlanService.setDisableStatus(id);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("successful"));
    }
}

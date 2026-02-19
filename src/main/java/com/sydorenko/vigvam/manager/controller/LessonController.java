package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.lessonsServices.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/lesson")
public class LessonController {

    private final MessageResponseDto messageResponseDto;
    private final LessonService lessonService;
    private final GenericService genericService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MessageResponseDto> createGenericLesson (@RequestBody CreateLessonRequestDto dto){
        genericService.checkAuditorByOrganization(dto.getOrganizationId().getId());
        lessonService.createGenericLesson(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

}

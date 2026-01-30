package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizations/crud")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;
    private final MessageResponseDto messageResponseDto;

    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponseDto> createOrganizationThisSettings(@RequestBody CreateOrganizationRequestDto dto){
        service.createOrganizationThisSettings(dto);
        messageResponseDto.setMessage("Organization created");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableClient(@RequestBody DisabledObjectRequestDto dto){
        service.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableClient(@RequestBody DisabledObjectRequestDto dto){
        service.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }
}

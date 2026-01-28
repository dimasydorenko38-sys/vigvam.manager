package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
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
    private final AuthResponseDto authResponseDto;

    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AuthResponseDto> createOrganizationThisSettings(@RequestBody CreateOrganizationRequestDto dto){
        service.createOrganizationThisSettings(dto);
        authResponseDto.setMessage("Organization created");
        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/disable")
    public ResponseEntity<AuthResponseDto> disableClient(@RequestBody DisabledObjectRequestDto dto){
        service.setDisableStatus(dto);
        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setMessage("Successful");
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/enable")
    public ResponseEntity<AuthResponseDto> enableClient(@RequestBody DisabledObjectRequestDto dto){
        service.setEnableStatus(dto);
        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setMessage("Successful");
        return ResponseEntity.ok(responseDto);
    }
}

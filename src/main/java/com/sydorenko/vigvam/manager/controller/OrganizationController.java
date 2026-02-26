package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.organizations.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.UpdateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.organizationsServices.OrganizationService;
import com.sydorenko.vigvam.manager.service.organizationsServices.SupportOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final OrganizationService organizationService;
    private final SupportOrganizationService supportOrganizationService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponseDto> createOrganizationThisSettings(@RequestBody CreateOrganizationRequestDto dto){
        organizationService.createOrganizationThisSettings(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Organization created"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponseDto> updateOrganization(@RequestBody UpdateOrganizationRequestDto dto){
        organizationService.updateOrganization(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableOrganization(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        organizationService.setDisableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableOrganization(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        organizationService.setEnableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/price/disable")
    public ResponseEntity<MessageResponseDto> disablePrice(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        supportOrganizationService.setDisableStatusPrice(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/price/enable")
    public ResponseEntity<MessageResponseDto> enablePrice(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        supportOrganizationService.setEnableStatusPrice(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }
}

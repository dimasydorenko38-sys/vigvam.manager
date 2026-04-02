package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.organizations.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.UpdateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.dto.response.OrganizationNamesResponseDto;
import com.sydorenko.vigvam.manager.service.organizationsServices.OrganizationService;
import com.sydorenko.vigvam.manager.service.organizationsServices.SupportOrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations/crud")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final SupportOrganizationService supportOrganizationService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponseDto> createOrganizationThisSettings(@Valid @RequestBody CreateOrganizationRequestDto dto){
        organizationService.createOrganizationThisSettings(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Organization created"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponseDto> updateOrganization(@Valid @RequestBody UpdateOrganizationRequestDto dto){
        organizationService.updateOrganization(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableOrganization(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        organizationService.setDisableStatus(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableOrganization(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        organizationService.setEnableStatus(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/price/invalidate")
    public ResponseEntity<MessageResponseDto> invalidatedPrice(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        supportOrganizationService.setInvalidatedPrice(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/price/enable")
    public ResponseEntity<MessageResponseDto> enablePrice(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        supportOrganizationService.setEnableStatusPrice(dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @GetMapping("/all_active_names")
    public ResponseEntity<List<OrganizationNamesResponseDto>> getAllOrganizationNames(){
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getAllOrganizationNames());
    }
}

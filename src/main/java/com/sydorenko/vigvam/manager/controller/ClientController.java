package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateLinkClientOrgRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusLinkClientOrgRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.request.CreateClientRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.usersServices.ClientService;
import com.sydorenko.vigvam.manager.service.usersServices.ClientsOrganizationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final MessageResponseDto messageResponseDto;
    private final ClientsOrganizationsService clientsOrganizationsService;
    private final GenericService genericService;

    @PostMapping("/add")
    public ResponseEntity<AuthResponseDto> createClient(@RequestBody CreateClientRequestDto dto){
        return ResponseEntity.ok(clientService.createClient(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/add/admin")
    public ResponseEntity<MessageResponseDto> createClientUsingAdmin(@RequestBody CreateClientRequestDto dto){
        clientService.createClient(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableClient(@RequestBody NewStatusObjectByIdRequestDto dto){
        clientService.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableClient(@RequestBody NewStatusObjectByIdRequestDto dto){
        clientService.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/add_organization")
    public ResponseEntity<MessageResponseDto> addLinkClientOrganization(@RequestBody CreateLinkClientOrgRequestDto dto){
        genericService.checkAuditorByOrganization(dto.organizationId());
        clientsOrganizationsService.addLinkClientOrganization(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/organization/disable")
    public ResponseEntity<MessageResponseDto> disableLink(@RequestBody NewStatusLinkClientOrgRequestDto dto){
        genericService.checkAuditorByOrganization(dto.organizationId());
        clientsOrganizationsService.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/organization/enable")
    public ResponseEntity<MessageResponseDto> enableClient(@RequestBody NewStatusLinkClientOrgRequestDto dto){
        genericService.checkAuditorByOrganization(dto.organizationId());
        clientsOrganizationsService.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }
}

package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.organizations.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.organizationsServices.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequestMapping("/api/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;
    private final MessageResponseDto messageResponseDto;

    @PostMapping("/add")
    public ResponseEntity<MessageResponseDto> createServiceType(@RequestBody CreateServiceTypeRequestDto dto){
        serviceTypeService.createServiceType(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableClient(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        serviceTypeService.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableClient(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        serviceTypeService.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

}

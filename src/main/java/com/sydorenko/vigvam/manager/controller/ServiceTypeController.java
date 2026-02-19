package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
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
    private void createServiceType(@RequestBody CreateServiceTypeRequestDto dto){
        serviceTypeService.createServiceType(dto);
    }

    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableClient(@RequestBody NewStatusObjectByIdRequestDto dto){
        serviceTypeService.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableClient(@RequestBody NewStatusObjectByIdRequestDto dto){
        serviceTypeService.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

}

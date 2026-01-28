package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;
    private final AuthResponseDto responseDto;

    @PostMapping("/add")
    private void createServiceType(@RequestBody CreateServiceTypeRequestDto dto){
        serviceTypeService.createServiceType(dto);
    }

    @PostMapping("/disable")
    public ResponseEntity<AuthResponseDto> disableClient(@RequestBody DisabledObjectRequestDto dto){
        serviceTypeService.setDisableStatus(dto);
        responseDto.setMessage("Successful");
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/enable")
    public ResponseEntity<AuthResponseDto> enableClient(@RequestBody DisabledObjectRequestDto dto){
        serviceTypeService.setEnableStatus(dto);
        responseDto.setMessage("Successful");
        return ResponseEntity.ok(responseDto);
    }

}

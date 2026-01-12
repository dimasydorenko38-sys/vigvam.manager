package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.service.ServiceTypeService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @PostMapping("/add")
    private void createServiceType(@RequestBody CreateServiceTypeRequestDto dto){
        serviceTypeService.createServiceType(dto);
    }

}

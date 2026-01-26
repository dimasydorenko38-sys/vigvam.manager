package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.request.CreateClientRequestDto;
import com.sydorenko.vigvam.manager.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/add")
    public ResponseEntity<AuthResponseDto> createClient(@RequestBody CreateClientRequestDto dto){
        return ResponseEntity.ok(clientService.createClient(dto));
    }
}

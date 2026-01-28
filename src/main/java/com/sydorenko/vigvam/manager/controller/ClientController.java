package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.dto.request.CreateClientRequestDto;
import com.sydorenko.vigvam.manager.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AuthResponseDto responseDto;

    @PostMapping("/add")
    public ResponseEntity<AuthResponseDto> createClient(@RequestBody CreateClientRequestDto dto){
        return ResponseEntity.ok(clientService.createClient(dto));
    }

    @PostMapping("/disable")
    public ResponseEntity<AuthResponseDto> disableClient(@RequestBody DisabledObjectRequestDto dto){
        clientService.setDisableStatus(dto);
        responseDto.setMessage("Successful");
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/enable")
    public ResponseEntity<AuthResponseDto> enableClient(@RequestBody DisabledObjectRequestDto dto){
        clientService.setEnableStatus(dto);
        responseDto.setMessage("Successful");
        return ResponseEntity.ok(responseDto);
    }

}

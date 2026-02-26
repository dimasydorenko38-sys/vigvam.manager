package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.users.CreateChildRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.usersServices.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/child")
public class ChildController {
    private final ChildService childeService;


    @PreAuthorize("hasRole('CLIENT')")
    @RequestMapping("/add/client")
    public ResponseEntity<MessageResponseDto> clientCreatesChild(@RequestBody CreateChildRequestDto dto){
        childeService.clientCreatesChild(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @RequestMapping("/add/admin")
    public ResponseEntity<MessageResponseDto> adminCreatesChild(@RequestBody CreateChildRequestDto dto){
        childeService.adminCreatesChild(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Successful"));
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableChilde(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        childeService.setDisableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableChilde(@RequestBody UpdateStatusObjectByIdRequestDto dto){
        childeService.setEnableStatus(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("Successful"));
    }
}

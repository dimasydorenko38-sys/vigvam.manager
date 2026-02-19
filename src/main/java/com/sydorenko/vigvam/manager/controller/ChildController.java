package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.CreateChildRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.MessageResponseDto;
import com.sydorenko.vigvam.manager.service.usersServices.ChildService;
import lombok.RequiredArgsConstructor;
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
    private final MessageResponseDto messageResponseDto;
    private final ChildService childeService;


    @PreAuthorize("hasRole('CLIENT')")
    @RequestMapping("/add/client")
    public ResponseEntity<MessageResponseDto> clientCreatesChild(@RequestBody CreateChildRequestDto dto){
        childeService.clientCreatesChild(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @RequestMapping("/add/admin")
    public ResponseEntity<MessageResponseDto> adminCreatesChild(@RequestBody CreateChildRequestDto dto){
        childeService.adminCreatesChild(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/disable")
    public ResponseEntity<MessageResponseDto> disableChilde(@RequestBody NewStatusObjectByIdRequestDto dto){
        childeService.setDisableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/enable")
    public ResponseEntity<MessageResponseDto> enableChilde(@RequestBody NewStatusObjectByIdRequestDto dto){
        childeService.setEnableStatus(dto);
        messageResponseDto.setMessage("Successful");
        return ResponseEntity.ok(messageResponseDto);
    }
}

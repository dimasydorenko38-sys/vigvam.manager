package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.users.RefreshTokenDto;
import com.sydorenko.vigvam.manager.dto.request.users.UserLoginDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import com.sydorenko.vigvam.manager.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final JwtService jwtService;

    @GetMapping("/all_source_client")
    public ResponseEntity<Map<SourceClient, String>> getAllSourceClient(){
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(SourceClient.values())
                .collect(Collectors.toMap(Function.identity(), SourceClient::getDisplayName)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshTokenUser(@RequestBody RefreshTokenDto dto){
        String token = jwtService.generateTokenByRefreshToken(dto);
        return  ResponseEntity.ok(new AuthResponseDto(token));
    };

    @PostMapping
    private ResponseEntity<AuthResponseDto> login(@RequestBody UserLoginDto dto){
        return ResponseEntity.ok(jwtService.loginUserByLogPass(dto));
    }
}

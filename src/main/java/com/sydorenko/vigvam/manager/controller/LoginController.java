package com.sydorenko.vigvam.manager.controller;

import com.sydorenko.vigvam.manager.dto.request.RefreshTokenDto;
import com.sydorenko.vigvam.manager.dto.request.UserLoginDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final JwtService jwtService;

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

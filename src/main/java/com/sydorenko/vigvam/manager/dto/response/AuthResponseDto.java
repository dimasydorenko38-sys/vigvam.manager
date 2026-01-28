package com.sydorenko.vigvam.manager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@NoArgsConstructor
public class AuthResponseDto {
    String token;
    UUID refreshToken;
    String message;



    public AuthResponseDto(String token) {
        this.token = token;
    }


    public AuthResponseDto(String token, UUID refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
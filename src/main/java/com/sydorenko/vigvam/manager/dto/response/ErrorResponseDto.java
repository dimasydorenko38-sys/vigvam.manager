package com.sydorenko.vigvam.manager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private LocalDateTime timestamp;

    private String message;

    private String fullMessage;
}

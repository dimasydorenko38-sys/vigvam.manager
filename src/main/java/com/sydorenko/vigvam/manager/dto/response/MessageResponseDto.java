package com.sydorenko.vigvam.manager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    private String message;

}

package com.sydorenko.vigvam.manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NonNull
    String login;
    @NonNull
    String password;
}

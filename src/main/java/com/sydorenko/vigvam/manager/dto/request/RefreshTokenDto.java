package com.sydorenko.vigvam.manager.dto.request;


import lombok.Getter;

import java.util.UUID;


@Getter
public class RefreshTokenDto{
    private UUID refreshToken;
}

package com.sydorenko.vigvam.manager.dto.request;


import lombok.Getter;

import java.util.UUID;

//{
//    "refreshToken": "UUID MY TOKEN FROM BD"
//        }

@Getter
public class RefreshTokenDto{
    private UUID refreshToken;
}

package com.sydorenko.vigvam.manager.dto.request.users.client;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClientRequestDto {

    @NonNull
    private Long clientId;
    @NonNull
    private String login;
    @NonNull
    private String password;
    @NonNull
    private String phone;
    @NonNull
    private String name;
    @NonNull
    private boolean photoPermission;
    private String sourceClient;
}
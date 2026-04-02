package com.sydorenko.vigvam.manager.dto.request.users.client;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClientRequestDto {

    @NonNull
    private Long clientId;
    @NotBlank(message = "Логін не можу бути порожнім")
    private String login;
    @NotBlank(message = "Пароль не можу бути порожнім")
    private String password;
    @NotBlank(message = "Телефон не можу бути порожнім")
    private String phone;
    @NotBlank(message = "Імʼя не можу бути порожнім")
    private String name;
    private boolean photoPermission;
    private String sourceClient;
}
package com.sydorenko.vigvam.manager.dto.request.users.client;

import com.sydorenko.vigvam.manager.dto.request.users.child.CreateChildRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequestDto {

    @NotBlank(message = "Логін не можу бути порожнім")
    private String login;
    @NotBlank(message = "Пароль не можу бути порожнім")
    private String password;
    @NotBlank(message = "Телефон не можу бути порожнім")
    private String phone;
    @NotBlank(message = "Імʼя не можу бути порожнім")
    private String name;
    @NonNull
    private Long organizationId;
    private boolean photoPermission;
    private String sourceClient;
    @Valid
    private Set<CreateChildRequestDto> children;
}

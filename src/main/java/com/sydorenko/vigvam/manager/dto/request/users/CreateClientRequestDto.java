package com.sydorenko.vigvam.manager.dto.request.users;

import lombok.*;

import java.util.Set;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequestDto {

    @NonNull
    private String login;
    @NonNull
    private String password;
    @NonNull
    private String phone;
    @NonNull
    private String name;
    @NonNull
    private Long organizationId;
    private boolean photoPermission;
    private String sourceClient;
    private Set<CreateChildRequestDto> children;
}

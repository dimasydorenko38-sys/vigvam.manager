package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequestDto {

    private String name;
    private String phone;
    private Set<RoleUser> roles;
    private Set<Long> organizationIds;
}

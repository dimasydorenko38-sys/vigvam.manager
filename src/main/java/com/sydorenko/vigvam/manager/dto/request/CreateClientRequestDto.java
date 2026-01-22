package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequestDto {

    private String login;
    private String password;
    private String phone;
    private String name;
    private OrganizationEntity organization;
    private boolean photoPermission;
    private String sourceClient;
    private Set<ChildEntity> children;
}

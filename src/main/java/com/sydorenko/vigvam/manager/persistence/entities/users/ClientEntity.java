package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "clients")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientEntity extends UserEntity {

    @NotEmpty(message = "Необхідно вказати принаймні одну організацію")
    @OneToMany
    @JoinColumn(name = "client_id")
    private Set<OrganizationEntity> organizations;

    @Column(name = "role", nullable = false)
    private RoleUser role;

    @Column(name = "feed_backs")
    private String feedBack;

    @Column(name = "photo_permissions", nullable = false)
    private boolean photoPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_sources", nullable = false)
    private SourceClient source;

    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL)
    private Set<ChildEntity> children;
}

package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import jakarta.persistence.*;
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


    @Column(name = "feed_backs")
    private String feedBack;

    @Column(name = "photo_permissions", nullable = false)
    private boolean photoPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_sources", nullable = false)
    private SourceClient source;

    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL)
    private Set<ChildEntity> childs;
}

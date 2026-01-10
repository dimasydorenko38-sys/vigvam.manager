package com.sydorenko.vigvam.manager.entities.users;

import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Table(name = "clients")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Client extends User {


    @Column(name = "feed_backs")
    private String feedBack;

    @Column(name = "photo_permissions", nullable = false)
    private boolean photoPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_sources", nullable = false)
    private SourceClient source;

    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Child> childs;

}

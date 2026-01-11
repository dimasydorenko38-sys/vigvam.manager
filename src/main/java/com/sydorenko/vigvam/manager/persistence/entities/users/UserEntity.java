package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.arhiv.RoleEntity;
import com.sydorenko.vigvam.manager.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", length = 20, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 15, nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "disabled_date")
    private LocalDate disabledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}

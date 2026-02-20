package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserEntity implements UserDetails, Statusable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", length = 20, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 15, nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Column(name = "disable_date")
    private LocalDateTime disableDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @UuidGenerator
    @Column(name = "refresh_token", unique = true)
    UUID refreshToken;

    @NonNull
    @Override
    public abstract Collection<? extends GrantedAuthority> getAuthorities();


    @NonNull
    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return Status.ENABLED.equals(this.status);
    }


}

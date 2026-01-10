package com.sydorenko.vigvam.manager.entities.users;

import com.sydorenko.vigvam.manager.entities.organizations.Organization;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.StatusUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "names", nullable = false)
    private String name;

    @Column(name = "phones", length = 20)
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "users_organizations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private Set<Organization> organization;

    @Column(name = "roles")
    private Set<RoleUser> role;

    @Column(name = "disabled_dates")
    private LocalDate disabledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusUser status;
}

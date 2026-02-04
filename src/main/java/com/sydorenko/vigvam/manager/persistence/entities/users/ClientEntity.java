package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "clients")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientEntity extends UserEntity {

    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<ClientsOrganizationsEntity> organizationLinks;

    @Column(name = "photo_permissions", nullable = false)
    private boolean photoPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_sources", nullable = false)
    private SourceClient source;

    @OneToMany (mappedBy = "client", cascade = CascadeType.ALL)
    private Set<ChildEntity> children;

    @Column(name = "role", nullable = false)
    private RoleUser role;

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("Role_"+this.role));
    }

}

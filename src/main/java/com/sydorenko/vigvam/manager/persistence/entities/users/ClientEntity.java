package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;


@Entity
@Table(name = "clients", indexes = {
        @Index(name = "idx_cliententity_status", columnList = "status")
})
@Getter
@Setter
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
    private Set<ChildEntity> children = new HashSet<>();

    @Column(name = "role", nullable = false)
    private RoleUser role;

    @Column(name = "created_by_client")
    private boolean createdByClient;

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("Role_"+this.role));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ClientEntity client = (ClientEntity) o;
        return getId() != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

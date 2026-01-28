package com.sydorenko.vigvam.manager.persistence.entities.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "employees")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends UserEntity {

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ContractEmployeeEntity> contractsEmployee;

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return contractsEmployee.stream()
                .map(contract -> new SimpleGrantedAuthority("ROLE_" + contract.getRole()))
                .collect(Collectors.toList());
    }
}

package com.sydorenko.vigvam.manager.persistence.entities.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeEntity extends UserEntity {

    @Column(name = "last_name", nullable = false)
    private String LastName;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ContractEmployeeEntity> contractsEmployee;

}

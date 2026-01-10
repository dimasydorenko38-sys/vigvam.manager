package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Entity
@Table(name = "employees")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeEntity extends UserEntity {

    @ElementCollection
    @CollectionTable(name = "employee_salary", joinColumns = @JoinColumn(name = "employee_id"))
    @MapKeyEnumerated(EnumType.STRING)
    private Map<ServiceType, Long> salary;

}

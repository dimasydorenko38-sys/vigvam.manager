package com.sydorenko.vigvam.manager.entities.users;

import com.sydorenko.vigvam.manager.enums.ServicesForPrice;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "admins")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter @Setter
public class Admin extends User {

    @ElementCollection
    @CollectionTable(name = "admin_salary", joinColumns = @JoinColumn(name = "admin_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "salary")
    private Map<ServicesForPrice, Long> salary;
}

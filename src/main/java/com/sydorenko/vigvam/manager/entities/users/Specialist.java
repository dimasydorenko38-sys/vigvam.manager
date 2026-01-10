package com.sydorenko.vigvam.manager.entities.users;

import com.sydorenko.vigvam.manager.enums.users.PositionSpecialist;
import com.sydorenko.vigvam.manager.enums.ServicesForPrice;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "specialists")
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Specialist extends User{

    @Enumerated(EnumType.STRING)
    @Column(name = "positions")
    private PositionSpecialist position;

    @ElementCollection
    @CollectionTable(name = "specialist_salary", joinColumns = @JoinColumn(name = "specialist_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "salary")
    private Map<ServicesForPrice, Long> salary;

}

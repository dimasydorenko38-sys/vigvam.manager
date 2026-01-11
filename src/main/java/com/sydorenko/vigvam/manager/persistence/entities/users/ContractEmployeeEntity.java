package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "contract_employee")
@Getter
@Setter
public class ContractEmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "organization_id",nullable = false)
    private OrganizationEntity organization;

    @ManyToOne
    @JoinColumn(name = "master_employee_id")
    private EmployeeEntity masterEmployee;

    @OneToMany(mappedBy = "contractEmployee", cascade = CascadeType.ALL)
    private Set<SalaryEmployeeEntity> salary;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleUser role;

    @Column(name = "disabled_date")
    private LocalDate disabledDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}

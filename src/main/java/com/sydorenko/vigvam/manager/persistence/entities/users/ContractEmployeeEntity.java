package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "contract_employee")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ContractEmployeeEntity implements Statusable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id",nullable = false)
    private OrganizationEntity organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_employee_id")
    private EmployeeEntity masterEmployee;

    @NotEmpty
    @OneToMany(mappedBy = "contractEmployee", cascade = CascadeType.ALL)
    private Set<SalaryEmployeeEntity> salary;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleUser role;

    @Column(name = "disable_date")
    private LocalDateTime disableDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}

package com.sydorenko.vigvam.manager.persistence.entities.organizations;

import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name", nullable = false, unique = true)
    private String organizationName;

    @Column(name = "organization_city", nullable = false)
    private String organizationCity;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "organization")
    private Set<ClientsOrganizationsEntity> clientLinks;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<ContractEmployeeEntity> employeeContracts;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<PriceOrganizationEntity> price;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyEnumerated(EnumType.STRING)
    @MapKey(name = "lessonType")
    private Map<LessonType, SettingLessonsTime> settingLessons;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private Long createdById;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by_id")
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    
}

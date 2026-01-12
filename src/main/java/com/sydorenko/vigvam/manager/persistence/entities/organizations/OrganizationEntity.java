package com.sydorenko.vigvam.manager.persistence.entities.organizations;

import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "organization_name", nullable = false, unique = true)
    private String organizationName;

    @Column(name = "organization_city", nullable = false)
    private String organizationCity;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<ContractEmployeeEntity> employeeContracts;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<PriceOrganizationEntity> price;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyEnumerated(EnumType.STRING)
    @MapKey(name = "lessonType")
    private Map<LessonType, SettingLessonsTime> settingLessons;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    
}

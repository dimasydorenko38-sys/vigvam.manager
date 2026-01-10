package com.sydorenko.vigvam.manager.persistence.entities.organizations;

import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.enums.ServiceType;
import com.sydorenko.vigvam.manager.enums.users.Status;
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

    @Column(name = "organization_city")
    private String organizationCity;

    @ManyToMany(mappedBy = "organizations")
    private Set<ClientEntity> clients;

    @ManyToMany(mappedBy = "organizations")
    private Set<EmployeeEntity> employees;

    @ElementCollection
    @CollectionTable(name = "organization_prices",
            joinColumns = @JoinColumn(name = "organization_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "prices", nullable = false)
    private Map<ServiceType, Long> prices;

    @ElementCollection
    @CollectionTable(name = "organization_lessons_settings",
            joinColumns = @JoinColumn(name = "organization_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "settingLessons", nullable = false)
    private Map<LessonType, SettingLessonsTime> settingLessons;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}

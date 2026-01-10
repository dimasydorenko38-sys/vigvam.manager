package com.sydorenko.vigvam.manager.entities.organizations;

import com.sydorenko.vigvam.manager.entities.users.Client;
import com.sydorenko.vigvam.manager.entities.users.Specialist;
import com.sydorenko.vigvam.manager.enums.ServicesForPrice;
import com.sydorenko.vigvam.manager.enums.users.StatusUser;
import com.sydorenko.vigvam.manager.enums.lessons.TypeLessons;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(name = "organizations")
@Getter @Setter
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_dates", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "organization_names", nullable = false, unique = true)
    private String organizationName;

    @Column(name = "organization_city")
    private String organizationCity;

    @ManyToMany(mappedBy = "organization")
    private Set<Client> clientSet;

    @ManyToMany(mappedBy = "organization")
    private Set<Specialist> specialistSet;

    @ElementCollection
    @CollectionTable(name = "organization_prices",
            joinColumns = @JoinColumn(name = "organization_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "prices", nullable = false)
    private Map<ServicesForPrice, Long> Price;

    @ElementCollection
    @CollectionTable(name = "organization_lessons_settings",
            joinColumns = @JoinColumn(name = "organization_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "settingLessons", nullable = false)
    private Map<TypeLessons, SettingLessonsTime> settingLessons;

    @Enumerated(EnumType.STRING)
    @Column(name = "statuses", nullable = false)
    private StatusUser statusUser;
}

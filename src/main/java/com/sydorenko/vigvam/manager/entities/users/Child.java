package com.sydorenko.vigvam.manager.entities.users;

import com.sydorenko.vigvam.manager.enums.users.StatusUser;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(name = "childs")
@Getter @Setter
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_dates", updatable = false, columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "names", nullable = false)
    private String Name;

    @Column(name = "last_names", nullable = false)
    private String lastName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "birthday_dates", nullable = false)
    private LocalDate birthdayDate;

    @Column(name = "interests")
    private String interests;

    @Column(name = "request_for_lessons")
    private String requestForLessons;

    @Column(name = "diagnosis")
    private String diagnosis;

    @ManyToOne
    @JoinColumn(name = "clients")
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_user", nullable = false)
    private StatusUser statusUser;
}

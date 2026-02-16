package com.sydorenko.vigvam.manager.persistence.entities.users;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "children")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildEntity implements Statusable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "birthday_date", nullable = false)
    private LocalDate birthdayDate;

    @Column(name = "interests")
    private String interests;

    @Column(name = "request_for_lessons")
    private String requestForLessons;

    @Column(name = "diagnosis")
    private String diagnosis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(name = "disable_date")
    private LocalDateTime disableDate;

    //TODO change ColumnName "status_user" for "status"

    @Enumerated(EnumType.STRING)
    @Column(name = "status_user", nullable = false)
    private Status status;

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
}

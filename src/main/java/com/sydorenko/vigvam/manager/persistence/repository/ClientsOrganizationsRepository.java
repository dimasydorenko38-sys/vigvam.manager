package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ChildNameResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ClientsOrganizationsRepository extends JpaRepository<ClientsOrganizationsEntity, LessonRepository> {
    @EntityGraph(attributePaths = {"organization"})
    List<ClientsOrganizationsEntity> findAllByClientId(Long clientId);

    @EntityGraph(attributePaths = {"client.children"})
    @Query("""
            SELECT c
            FROM ClientsOrganizationsEntity c
            WHERE c.status = :status
            AND c.organization.id = :orgId
            """)
    List<ClientsOrganizationsEntity> findAllByOrgIdAndClientStatus(
            @Param("status")Status clientStatus,
            @Param("orgId") Long organizationId);
}

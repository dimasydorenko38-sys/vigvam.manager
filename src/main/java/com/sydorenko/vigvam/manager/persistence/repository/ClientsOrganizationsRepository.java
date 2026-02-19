package com.sydorenko.vigvam.manager.persistence.repository;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientsOrganizationsRepository extends JpaRepository<ClientsOrganizationsEntity, Long> {

    @EntityGraph(attributePaths = {"organization"})
    List<ClientsOrganizationsEntity> findAllByClientIdAndStatus(Long clientId, Status status);
    default List<ClientsOrganizationsEntity> findAllActiveWithOrgByClientId(Long clientId){
        return findAllByClientIdAndStatus(clientId, Status.ENABLED);
    };

    @EntityGraph(attributePaths = {"client.children"})
    @Query("""
            SELECT c
            FROM ClientsOrganizationsEntity c
            WHERE c.status = :status
            AND c.organization.id = :orgId
            """)
    List<ClientsOrganizationsEntity> findAllByClientStatusAndOrgId(
            @Param("status") Status clientStatus,
            @Param("orgId") Long organizationId);

    boolean existsByClientIdAndOrganizationIdAndStatus(Long clientId, Long organizationId, Status status);

    Optional<ClientsOrganizationsEntity> findByClientIdAndOrganizationId(Long clientId, Long OrganizationId);
}

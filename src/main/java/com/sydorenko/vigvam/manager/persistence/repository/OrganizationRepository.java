package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long>, GenericActiveRepository<OrganizationEntity> {

    @EntityGraph(attributePaths = {"settingLessons"})
    @Query("""
            SELECT o FROM OrganizationEntity o
            WHERE o.id = :id
            AND o.status = :status
            """)
    Optional<OrganizationEntity> findActiveByIdWithSettings(Long id, Status status);
    default Optional<OrganizationEntity> findActiveByIdWithSettings(Long id){
        return findActiveByIdWithSettings(id, Status.ENABLED);
    }

    @EntityGraph(attributePaths = {"settingLessons","price"})
    @Query("""
            SELECT o FROM OrganizationEntity o
            WHERE o.id = :id
            AND o.status = :status
            """)
    Optional<OrganizationEntity> findActiveByIdWithSettingsAndPrice(Long id, Status status);
    default Optional<OrganizationEntity> findActiveByIdWithSettingsAndPrice(Long id){
        return findActiveByIdWithSettingsAndPrice(id, Status.ENABLED);
    }

}

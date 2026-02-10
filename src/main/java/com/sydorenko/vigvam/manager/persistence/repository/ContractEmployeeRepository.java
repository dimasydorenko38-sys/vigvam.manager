package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.EmployeeNameResponseProjection;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractEmployeeRepository extends JpaRepository<ContractEmployeeEntity,Long> {

    List<ContractEmployeeEntity> findAllByOrganizationId(Long id);
    List<ContractEmployeeEntity> findAllByEmployeeId(Long id);

    @EntityGraph(attributePaths = {"employee", "salary", "organization"})
    List<ContractEmployeeEntity> findAllWithDetailsByEmployeeId(Long id);

    @EntityGraph(attributePaths = {"organization"})
    List<ContractEmployeeEntity> findAllWithOrgByEmployeeId(Long id);

    @Query("""
            SELECT
            c.employee.id AS id,
            c.employee.name AS name,
            c.employee.lastName AS lastName
            FROM ContractEmployeeEntity c
            WHERE c.role = :role
            AND c.status = :status
            AND c.organization.id = :orgId
            AND c.employee.status = :status
            """)
    List<EmployeeNameResponseProjection> findAllEmployeesNameByOrganizationId(
            @Param("orgId") Long organizationId,
            @Param("role")RoleUser roleUser,
            @Param("status")Status status
    );

}


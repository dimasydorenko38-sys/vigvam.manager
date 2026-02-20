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

    @EntityGraph(attributePaths = {"employee", "salary", "organization"})
    List<ContractEmployeeEntity> findAllWithDetailsByEmployeeIdAndStatus(Long id, Status status);
    default     List<ContractEmployeeEntity> findAllActiveWithDetailsByEmployeeId(Long id){
        return findAllWithDetailsByEmployeeIdAndStatus(id, Status.ENABLED);
    }


    @EntityGraph(attributePaths = {"organization"})
    List<ContractEmployeeEntity> findAllWithOrgByEmployeeIdAndStatus(Long id, Status status);
    default List<ContractEmployeeEntity> findAllActiveWithOrgByEmployeeId(Long id){
        return findAllWithOrgByEmployeeIdAndStatus(id, Status.ENABLED);
    }

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

    boolean existsByEmployeeIdAndOrganizationIdAndRoleAndStatus(Long idEmp, Long idOrg, RoleUser role, Status status);
}


package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractEmployeeRepository extends JpaRepository<ContractEmployeeEntity,Long> {

    List<ContractEmployeeEntity> findAllByOrganizationId(Long id);

    List<ContractEmployeeEntity> findAllByEmployeeId(Long id);
}


package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}

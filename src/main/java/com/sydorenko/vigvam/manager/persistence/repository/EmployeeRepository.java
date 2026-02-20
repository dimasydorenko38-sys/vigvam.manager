package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.EmployeeNameResponseProjection;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EmployeeRepository extends UserRepository<@NonNull EmployeeEntity> {

    Set<EmployeeNameResponseProjection> findAllByIdIn(Set<Long> ids);

}

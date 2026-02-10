package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

/**
 * Projection for {@link com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity}
 */
public interface EmployeeNameResponseProjection {
        Long getId();
        String getName();
        String getLastName();

}
package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildNameResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String secondName;



    public ChildNameResponseDto(ChildEntity child) {
        this.id = child.getId();
        this.name = child.getName();
        this.lastName = child.getLastName();
        this.secondName = child.getSecondName();
    }
}

package com.sydorenko.vigvam.manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewStatusObjectByIdRequestDto {
    @NonNull
    private Long id;
}

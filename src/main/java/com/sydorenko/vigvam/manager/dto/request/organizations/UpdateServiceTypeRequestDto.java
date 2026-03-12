package com.sydorenko.vigvam.manager.dto.request.organizations;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateServiceTypeRequestDto {

    @NonNull
    private Long serviceTypeId;
    @Pattern(regexp = "^[-_a-zA-Z]*$", message = "Дозволені лише латинські літери")
    private String serviceType;

    private String displayName;
}

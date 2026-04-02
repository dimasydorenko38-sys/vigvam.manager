package com.sydorenko.vigvam.manager.dto.request.organizations;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Системна назва послуги не можу бути порожнім")
    private String serviceType;

    @NotBlank(message = "Вкажіть назву послуги, так Вона буде відображатися на екрані")
    private String displayName;
}

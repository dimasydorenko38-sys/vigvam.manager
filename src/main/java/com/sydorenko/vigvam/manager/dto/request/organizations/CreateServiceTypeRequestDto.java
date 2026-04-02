package com.sydorenko.vigvam.manager.dto.request.organizations;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


//      {
//        "serviceTypeId": "art-terapia",
//        "displayName": "арт терапія"
//        }


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceTypeRequestDto {

    @Pattern(regexp = "^[-_a-zA-Z]*$", message = "Дозволені лише латинські літери")
    @NotBlank(message = "Заповніть системну назву послуги")
    private String serviceType;

    @NotBlank(message = "Вкажіть назву, що буде відображена на екрані застосунку")
    private String displayName;
}

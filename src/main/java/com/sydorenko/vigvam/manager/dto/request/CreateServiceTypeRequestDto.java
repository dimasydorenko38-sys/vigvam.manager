package com.sydorenko.vigvam.manager.dto.request;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;




//      {
//        "serviceType": "art-terapia",
//        "displayName": "арт терапія"
//        }



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceTypeRequestDto {

    @Pattern(regexp = "^[-_a-zA-Z]*$", message = "Дозволені лише латинські літери")
    private String serviceType;

    private String displayName;
}

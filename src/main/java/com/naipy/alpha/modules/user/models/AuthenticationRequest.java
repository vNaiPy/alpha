package com.naipy.alpha.modules.user.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}

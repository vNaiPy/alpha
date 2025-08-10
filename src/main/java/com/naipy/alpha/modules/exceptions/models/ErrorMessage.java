package com.naipy.alpha.modules.exceptions.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naipy.alpha.modules.exceptions.enums.ErrorAppType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "tb_errors_app")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String message;

    @NotBlank
    private String technicalError;

    @NotBlank
    private Integer lineNumberError;

    @NotNull
    private ErrorAppType errorType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant moment;

}

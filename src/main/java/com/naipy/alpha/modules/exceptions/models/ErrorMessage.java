package com.naipy.alpha.modules.exceptions.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.naipy.alpha.modules.exceptions.enums.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

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
    private UUID id;

    @NotNull
    private String message;

    @NotNull
    private String resultCode;

    @NotNull
    private ErrorType errorType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant moment;

}

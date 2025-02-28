package me.ryzeon.finanzas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponse {

    @Schema(description = "Token JWT generado tras autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5...")
    private String token;

    @Schema(description = "Identificador del usuario autenticado, puede ser id, correo, o user", example = "john.doe")
    private String userIdentifier;
}
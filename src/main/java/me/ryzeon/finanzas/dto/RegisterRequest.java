package me.ryzeon.finanzas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegisterRequest {

    @Schema(description = "Nombre de usuario", example = "johndoe")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "johndoe@example.com")
    @Email(message = "Debe proporcionar un correo válido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "mypassword123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
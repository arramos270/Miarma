package com.example.Miarma.Dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {

    @NotBlank(message = "El nombre de usuario no puede estar en blanco")
    private String username;

    @URL //Para seguir con que sea una URL
    private String avatar;

    @NotEmpty(message = "El nombre completo no puede estar en blanco")
    private String fullname;

    @Email(message = "No es un email válido")
    private String email;

    @NotBlank
    @Min(value = 4, message = "La contraseña debe tener al menos 4 carácteres")
    private String password;
    private String password2;

}

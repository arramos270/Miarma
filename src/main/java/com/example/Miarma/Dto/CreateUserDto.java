package com.example.Miarma.Dto;

import com.example.Miarma.Validation.CustomValidators.UniqueUsername;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder //Falta comprobar que las contraseñas son iguales
public class CreateUserDto {

    @NotBlank(message = "{user.username.blank}")
    @UniqueUsername(message = "{user.username.unique}")
    private String username;

    @Email(message = "{user.email.notemail}")
    private String email;

    @Past
    private Date fechaNacimiento;

    @URL(message = "{user.avatar.url}")
    private String avatar;

    @NotEmpty(message = "{user.fullname.empty}")
    private String fullname;

    @NotBlank(message = "{user.password.blank")
    @Min(value = 4, message = "{user.password.min}")
    private String password;
    private String password2;

}

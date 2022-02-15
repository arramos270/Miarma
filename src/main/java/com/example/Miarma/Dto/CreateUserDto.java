package com.example.Miarma.Dto;

import com.example.Miarma.Validation.CustomValidators.UniqueUsername;
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

    @NotBlank(message = "{user.username.blank}")
    @UniqueUsername(message = "{user.username.unico}")
    private String username;

    @URL(message = "{user.avatar.url}")
    private String avatar;

    @NotEmpty(message = "{user.fullname.empty}")
    private String fullname;

    @Email(message = "{user.email.notemail}")
    private String email;

    @NotBlank(message = "{user.password.blank")
    @Min(value = 4, message = "{user.password.min}")
    private String password;
    private String password2;

}

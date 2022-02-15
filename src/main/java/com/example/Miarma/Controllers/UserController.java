package com.example.Miarma.Controllers;

import com.example.Miarma.Dto.CreateUserDto;
import com.example.Miarma.Dto.GetUserDto;
import com.example.Miarma.Dto.UserDtoConverter;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/user")
public class UserController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;

    @PostMapping("/auth/register")
    public ResponseEntity<GetUserDto> nuevoUsuario(@Valid @RequestBody CreateUserDto newUser) {
        UserEntity saved = userEntityService.save(newUser);

        if (saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(saved));

    }
}

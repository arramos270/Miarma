package com.example.Miarma.Controllers;

import com.example.Miarma.Dto.CreateUserDto;
import com.example.Miarma.Dto.GetUserDto;
import com.example.Miarma.Dto.UserDtoConverter;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;

    @PostMapping("/auth/register")
    public ResponseEntity<GetUserDto> nuevoUsuario(@RequestBody CreateUserDto newUser) {
        UserEntity saved = userEntityService.save(newUser);

        if (saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> buscarUsuario(@PathVariable @Min(value = 0, message = "No se pueden buscar usuarios con un identificador negativo") UUID id) {

        return userEntityService.findById(id).ifPresent( userEntityService.findById(id)); //Mirar devolver cosas con un Optional.ifPresent()

        if (userEntityService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(userEntityService.findById(id).ifPresent()));
        }
    }
}

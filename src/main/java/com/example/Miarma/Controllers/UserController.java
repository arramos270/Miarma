package com.example.Miarma.Controllers;

import com.example.Miarma.Dto.CreateUserDto;
import com.example.Miarma.Dto.GetUserDto;
import com.example.Miarma.Dto.UserDtoConverter;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Security.CurrentUser;
import com.example.Miarma.Security.UserPrincipal;
import com.example.Miarma.Services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/user")
@Validated
public class UserController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;

    @PostMapping("/auth/register") //Como tenemos la clase anotada con @Validated, no nos hace falta poner aqui @Valid
    public ResponseEntity<GetUserDto> nuevoUsuario(@RequestBody CreateUserDto newUser) {
        UserEntity saved = userEntityService.save(newUser);

        if (saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(saved));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<GetUserDto> logearUsuario(@RequestBody CreateUserDto newUser) {
        UserEntity saved = userEntityService.save(newUser);

        if (saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(saved));
    }

    @GetMapping("/me") //El usuario logeado ve su perfil
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GetUserDto> verMiPerfil(@CurrentUser UserPrincipal currentUser) {
        Optional<UserEntity> miUsuario = userEntityService.findById(currentUser.getId());
            if (miUsuario.isPresent()){
                return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(miUsuario.get()));
            } else {
                return ResponseEntity.badRequest().build();
            }

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> buscarUsuarioPorId(@PathVariable @Min(value = 0, message = "No se pueden buscar usuarios con un identificador negativo") UUID id) {
        Optional<UserEntity> usuarioBuscado = userEntityService.findById(id);

        if(usuarioBuscado.isPresent()) {
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(usuarioBuscado.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

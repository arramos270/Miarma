package com.example.Miarma.controllers;

import com.example.Miarma.dto.GetUserDto;
import com.example.Miarma.dto.UserDtoConverter;
import com.example.Miarma.models.*;
import com.example.Miarma.security.CurrentUser;
import com.example.Miarma.security.UserPrincipal;
import com.example.Miarma.services.SeguimientoService;
import com.example.Miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class UserEntityController {

    private UserEntityService userEntityService;
    private SeguimientoService seguimientoService;
    private UserDtoConverter userDtoConverter;

    @GetMapping("/profile/{id}") //El quién es quién de los métodos
    public ResponseEntity<GetUserDto> verPerfilUsuario(@PathVariable UUID id, @CurrentUser UserPrincipal currentUser){
        Optional<UserEntity> usuarioBuscado = userEntityService.findById(id);
        UserEntity usuarioActual = userEntityService.findById(currentUser.getId()).get();

        //Si ese usuario no existe
        if(usuarioBuscado.isEmpty()){
            return ResponseEntity.notFound().build();

        //Si ese usuario buscado existe
        } else {
            GetUserDto usuarioBuscadoDto = userDtoConverter.convertUserEntityToGetUserDto(usuarioBuscado.get());

            //El usuario buscado tiene un perfil público?

            //Sí
            if(usuarioBuscado.get().getPerfilPublico() == true){ //
                return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscadoDto);

            //No, el usuario tiene un perfil privado
            } else {
                //Sigo a ese usuario?

                //Sí lo sigo
                if (usuarioBuscado.get().getFollowers().contains(usuarioActual)) {
                    return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscadoDto);

                //No lo sigo
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        }
    }

    @PutMapping("/profile/me") //Misma pega que al editar un post: no podría cambiar sólo un parámetro, sino que deben ser todos
    //Por motivos obvios, no dejaremos que se puedan cambiar manualmente el id, los seguidores y seguidos,
    //el rol y la fecha de creación (pues la fecha de última modificación de la contraseña pasará a ser ahora)
    public ResponseEntity<UserEntity> editarMe(@CurrentUser UserPrincipal currentUser, @RequestBody String username, String email, Date fechaNacimiento, String password, String avatar, String fullname, boolean perfilPublico){
        UserEntity usuarioEditado = UserEntity.builder()
                .id(currentUser.getId()) //Pues el método edit se guía por id, y este es clave primaria y por tanto único
                .username(username)
                .email(email)
                .fechaNacimiento(fechaNacimiento)
                .password(password)
                .avatar(avatar)
                .perfilPublico(perfilPublico)
                .lastPasswordChangeAt(LocalDateTime.now())
                .build();

            userEntityService.edit(usuarioEditado);

            return ResponseEntity.status(HttpStatus.OK).body(usuarioEditado);
    }

    @PostMapping("/follow/{nick}")
    public ResponseEntity<Seguimiento> crearPeticion(@PathVariable String nick, @CurrentUser UserPrincipal currentUser) {
        Optional<UserEntity> usuarioASeguir = userEntityService.findUserByUsername(nick);

        //Existe ese usuario?

        //No
        if(usuarioASeguir.isEmpty()){
            return ResponseEntity.notFound().build();

        //Sí
        } else {
            Seguimiento peticionDeSeguimiento = Seguimiento.builder()
                    .id(new SeguimientoPK(currentUser.getId(), usuarioASeguir.get().getId()))
                    .seguido(usuarioASeguir.get())
                    .seguidor(userEntityService.findById(currentUser.getId()).get())
                    .estado(EstadoPeticion.EN_ESPERA)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(peticionDeSeguimiento);
        }
    }

    @PostMapping("/follow/accept/{idPeticion}")
    public ResponseEntity<UserEntity> aceptarPeticion(@PathVariable UUID idPeticion, @CurrentUser UserPrincipal currentUser) {
        Optional<Seguimiento> peticionAAceptar = seguimientoService.findById(idPeticion);

        //Existe esa petición?

        //Sí
        if (peticionAAceptar.isPresent()) {
        UserEntity newFollower = peticionAAceptar.get().getSeguidor();
        UserEntity toBeFollow = peticionAAceptar.get().getSeguido();

        //Eres tú el usuario al que le ha llegado la solicitud?

            //Sí
            if (toBeFollow == userEntityService.findById(currentUser.getId()).get()) {

                    toBeFollow.getFollowers().add(newFollower); //Ahora te sigue alguien nuevo
                    newFollower.getFollows().add(toBeFollow); //Ahora sigues al que le enviaste la solicitud

                    seguimientoService.delete(peticionAAceptar.get());

                    return ResponseEntity.status(HttpStatus.CREATED).body(newFollower);

            //No eres el usuario al que le corresponde aceptarla
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        //Esa petición no existe
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/follow/decline/{idPeticion}") //En la documentación pone que es un POST, pero como borramos una entidad, creo que debería ser un DELETE
    public ResponseEntity<UserEntity> denegarPeticion(@PathVariable UUID idPeticion, @CurrentUser UserPrincipal currentUser) {
        Optional<Seguimiento> peticionABorrar = seguimientoService.findById(idPeticion);

        //Existe esa petición?

        //Sí
        if (peticionABorrar.isPresent()) {
            UserEntity newFollower = peticionABorrar.get().getSeguidor();
            UserEntity toBeFollow = peticionABorrar.get().getSeguido();

            //Eres tú el usuario al que le ha llegado la solicitud?

            //Sí
            if (toBeFollow == userEntityService.findById(currentUser.getId()).get()) {

                seguimientoService.delete(peticionABorrar.get());

                return ResponseEntity.status(HttpStatus.OK).build();

                //No eres el usuario al que le corresponde aceptarla
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            //Esa petición no existe
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/follow/list") //Entiendo que "todas las peticiones de seguimiento" a ESTE USUARIO
    public ResponseEntity<List<UserEntity>> verListasSeguimientos(@CurrentUser UserPrincipal currentUser) {
        //Realmente no hacía falta hacer "estados" de los seguimientos, pues al completarse o denegarse, el seguimiento se borra
        //Pese a ello, por lógica suponemos que queremos listar todos los usuarios que nos han mandado una petición y esta sigue en espera

        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.peticionesDeSeguimientoAMi(currentUser.getId()));
    }
}

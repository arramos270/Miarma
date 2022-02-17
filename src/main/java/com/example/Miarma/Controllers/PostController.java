package com.example.Miarma.Controllers;

import com.example.Miarma.Models.Post;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Security.CurrentUser;
import com.example.Miarma.Security.UserPrincipal;
import com.example.Miarma.Services.PostService;
import com.example.Miarma.Services.StorageService;
import com.example.Miarma.Services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Validated
public class PostController {

    private FileController fileController;
    private StorageService storageService;
    private PostService postService;
    private UserEntityService userEntityService;

    @PostMapping("/")
    public ResponseEntity<Post> crearPost(@RequestBody String titulo, String descripcion, boolean publico, MultipartFile archivo, @CurrentUser UserPrincipal currentUser){

        Post nuevoPost = Post.builder()
            .idCreador(currentUser.getId())
            .title(titulo)
            .description(descripcion)
            .publica(publico)
            .archivo(fileController.upload(archivo).getBody())
            .escalado(fileController.uploadCustom(archivo, 1024L).getBody()) //Así podemos usar este método también para escalar el avatar del usuario
            .build();

        postService.save(nuevoPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPost);
    }

    @PutMapping("/{id}") //La única "pega" de esto, es que no podría cambiar sólo un parámetro, sino que deben ser todos
    public ResponseEntity<Post> editarPost(@PathVariable UUID id, @RequestBody String titulo, String descripcion, MultipartFile archivo){
        Optional<Post> postABorrar = postService.findById(id);

        if(postABorrar.isPresent()) {
            fileController.deleteFile(postABorrar.get().getArchivo().getName()); //Primero borramos las imagenes antiguas
            fileController.deleteFile(postABorrar.get().getEscalado().getName());

            Post postEditado = Post.builder()
                    .id(id)
                    .title(titulo)
                    .description(descripcion)
                    .archivo(fileController.upload(archivo).getBody())
                    .escalado(fileController.uploadCustom(archivo, 1024L).getBody())
                    .build();

            postService.edit(postEditado); //Pues el editar ya busca y sobreescribe, pues el UUID es único


            return ResponseEntity.status(HttpStatus.OK).body(postEditado);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity borrarPost(@PathVariable UUID id){
        Optional<Post> postABorrar = postService.findById(id);

        if(postABorrar.isPresent()) {
            fileController.deleteFile(postABorrar.get().getArchivo().getName()); //Primero borramos las imagenes
            fileController.deleteFile(postABorrar.get().getEscalado().getName());

            postService.delete(postABorrar.get());

            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public")
    public ResponseEntity<List<Post>> verPostsPublicos() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.listarAllPostPublicos());
    }

    @GetMapping("/{id}") //Necesitamos que los Post contengan a su creador
    public ResponseEntity<List<Post>> verPostPorId(@PathVariable UUID id, @CurrentUser UserPrincipal currentUser) {
        UserEntity usuarioActual = userEntityService.findById(currentUser.getId()).get();
        Optional<Post> postAVer = postService.findById(id);

        if(postAVer.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            if (usuarioActual.getFollows().contains(userEntityService.findById(postAVer.get().getIdCreador()))) //Miramos si este usuario sigue al creador del post
                return ResponseEntity.status(HttpStatus.OK).body(postService.listarAllPostPublicos());
            else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();//Pues no sigue a ese usuario
            }
        }
    }

    @GetMapping("/{nick}")
    public ResponseEntity<List<Post>> verPostDeUsuario(@PathVariable String username, @CurrentUser UserPrincipal currentUser) {
        UserEntity usuarioABuscar = userEntityService.findUserByUsername(username).get();

        if(userEntityService.findUserByUsername(username).isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            if (usuarioABuscar.getFollowers().contains(userEntityService.findById(currentUser.getId()))) //Miramos si el usuario a buscar es seguido por el que busca
                return ResponseEntity.status(HttpStatus.OK).body(postService.listarTodosPostsUsuarioX(usuarioABuscar.getId())); //Le sigue? Todos sus posts
            else{
                return ResponseEntity.status(HttpStatus.OK).body(postService.listarTodosPostsPublicosUsuarioX(usuarioABuscar.getId()));//No le sigue? sólo los posts públicos
            }
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<Post>> verMisPosts(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.listarTodosPostsUsuarioX(currentUser.getId()));
    }


}

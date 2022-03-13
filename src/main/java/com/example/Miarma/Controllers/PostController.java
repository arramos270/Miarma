package com.example.Miarma.Controllers;

import com.example.Miarma.Dto.CreatePostDto;
import com.example.Miarma.Dto.GetPostDto;
import com.example.Miarma.Dto.PostDtoConverter;
import com.example.Miarma.Models.Post;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Security.CurrentUser;
import com.example.Miarma.Security.UserPrincipal;
import com.example.Miarma.Services.PostService;
import com.example.Miarma.Services.StorageService;
import com.example.Miarma.Services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Validated
public class PostController {

    private FileController fileController;
    private StorageService storageService;
    private PostService postService;
    private PostDtoConverter postDtoConverter;
    private UserEntityService userEntityService;

    @PostMapping("/")
    public ResponseEntity<GetPostDto> crearPost(@RequestPart CreatePostDto nuevoPost, @RequestPart MultipartFile archivo, @AuthenticationPrincipal UserEntity creador) throws Exception {

        Post nuevo = postService.saveNewPost(nuevoPost, creador, archivo);

        if(nuevo == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(postDtoConverter.convertPostToGetPostDto(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPostDto> editarPost(@PathVariable UUID id, @RequestPart CreatePostDto nuevoPost, @RequestPart MultipartFile file, @AuthenticationPrincipal UserEntity usuario) throws Exception{

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.editPost(nuevoPost, file, id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity borrarPost(@PathVariable UUID id, @AuthenticationPrincipal UserEntity usuario){

        postService.deletePublicacion(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public")
    public ResponseEntity<List<Post>> verPostsPublicos() {

        return ResponseEntity.status(HttpStatus.OK).body(postService.listarAllPostPublicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostDto> verPostPorId(@PathVariable UUID id, @AuthenticationPrincipal UserEntity usuario) {
        Optional<Post> postOptional = postService.findById(id);

        if(postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Post post = postOptional.get();
            UUID propietarioId = post.getPropietario().getId();

            if(propietarioId.equals(usuario.getId()) || post.isPublica()
                    || usuario.getFollows().contains(post.getPropietario())) { //Si sigues al propietario de la foto, lo ves
                return ResponseEntity.ok()
                        .body(postDtoConverter.convertPostToGetPostDto(post));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @GetMapping("/{nick}")
    public ResponseEntity<List<GetPostDto>> verPostDeUsuario(@PathVariable String nick, @AuthenticationPrincipal UserEntity usuario) {
        UserEntity usuarioBuscado = userEntityService.findUserByUsername(nick).get();

        List<Post> publicaciones;

        if(usuario.getFollows().contains(usuarioBuscado)) {
            publicaciones = postService.listarTodosPostsUsuarioX(usuarioBuscado.getId());
        } else {
            publicaciones = postService.listarTodosPostsPublicosUsuarioX(usuarioBuscado.getId());
        }

        if(publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(
                    publicaciones.stream()
                            .map(postDtoConverter::convertPostToGetPostDto)
                            .collect(Collectors.toList())
            );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<GetPostDto>> verMisPosts(@AuthenticationPrincipal UserEntity usuario) {
        List<Post> misPublicaciones = postService.listarTodosPostsUsuarioX(usuario.getId());

        if(misPublicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(
                    misPublicaciones.stream()
                            .map(postDtoConverter::convertPostToGetPostDto)
                            .collect(Collectors.toList())
            );
        }
    }
}

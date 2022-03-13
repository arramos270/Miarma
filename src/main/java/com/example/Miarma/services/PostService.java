package com.example.Miarma.services;

import com.example.Miarma.dto.CreatePostDto;
import com.example.Miarma.dto.GetPostDto;
import com.example.Miarma.dto.PostDtoConverter;
import com.example.Miarma.exception.SingleEntityNotFoundException;
import com.example.Miarma.models.Post;
import com.example.Miarma.models.UserEntity;
import com.example.Miarma.repositories.PostRepository;
import com.example.Miarma.utils.MediaTypeSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class PostService extends BaseService<Post, UUID, PostRepository> {

    private final StorageService storageService;
    private final PostRepository postRepository;
    private final PostDtoConverter postDtoConverter;
    private final MediaTypeSelector mediaTypeSelector;
    private MultipartFile file;

    public List<Post> listarAllPostPublicos(){
        return repositorio.listaPostPublicos();
    }

    public List<Post> listarTodosPostsUsuarioX(UUID id){
        return repositorio.listaPostDeUsuario(id);
    }

    public List<Post> listarTodosPostsPublicosUsuarioX(UUID id){
        return repositorio.listaPostPublicosDeUsuario(id);
    }

    public Post saveNewPost(CreatePostDto nuevoPost, UserEntity usuario,
                                MultipartFile media) throws Exception {
        file = mediaTypeSelector.selectMediaType(media);

        String originalFilename = storageService.store(media);
        String scaledFilename = storageService.store(file);

        String originalUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(originalFilename)
                .toUriString();

        String escaladaUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(scaledFilename)
                .toUriString();

        Post post = Post.builder()
                .title(nuevoPost.getTitle())
                .description(nuevoPost.getDescription())
                .archivo(originalUri)
                .escalado(escaladaUri)
                .publica(nuevoPost.isPublic())
                .propietario(usuario)
                .build();

        return save(post);
    }

    public GetPostDto editPost(CreatePostDto post, MultipartFile archivo, UUID id, UserEntity usuario) throws Exception {
        Optional<Post> postOptional = findById(id);

        if (postOptional.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            Post postAntiguo = postOptional.get();

            if (usuario.getId().equals(postAntiguo.getPropietario().getId())) {
                file = mediaTypeSelector.selectMediaType(archivo);

                String originalFilename = storageService.store(archivo);
                String scaledFilename = storageService.store(file);

                String originalUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(originalFilename)
                        .toUriString();

                String escaladoUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(scaledFilename)
                        .toUriString();

                Post postNuevo = postDtoConverter
                        .convertCreatePostDtoToPost(post, postAntiguo, escaladoUri, originalUri);

                edit(postNuevo);

                return GetPostDto.builder()
                        .id(postAntiguo.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .archivo(originalUri)
                        .escalado(escaladoUri)
                        .isPublic(post.isPublic())
                        .nombreCreador(postAntiguo.getPropietario().getUsername())
                        .fechaPublicacion(postAntiguo.getFechaPublicacion())
                        .build();
            } else {
                throw new RuntimeException("Error de procesamiento");
            }
        }
    }

    public void deletePublicacion(UUID id, UserEntity usuario) {
        Optional<Post> postOptional = findById(id);

        if(postOptional.isEmpty()) {
            throw new SingleEntityNotFoundException(id.toString(), Post.class);
        } else {
            Post post = postOptional.get();

            if (usuario.getId().equals(post.getPropietario().getId())) {
                storageService.deleteFile(post.getEscalado());
                delete(post);
            } else {
                throw new RuntimeException("Error de procesamiento");
            }
        }
    }
}

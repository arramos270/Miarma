package com.example.Miarma.Controllers;

import com.example.Miarma.Models.Post;
import com.example.Miarma.Services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Validated
public class PostController {

    private FileController fileController;
    private StorageService storageService;

    @PostMapping("/")
    public ResponseEntity<Post> crearPost(@RequestBody String titulo, String descripcion, boolean publico, MultipartFile archivo){
        //FileResponse archivoNormal = fileController.upload(archivo);
        //FileResponse archivoEscalado = fileController.uploadCustom(archivo, 1024L);

        Post nuevoPost = Post.builder()
            .title(titulo)
            .description(descripcion)
            .publica(publico)
            .archivo(storageService.store(archivo))
            .escalado(storageService.storeCustomSize(archivo, 1024L))
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPost);

    }

}

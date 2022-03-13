package com.example.Miarma.controllers;

import com.example.Miarma.dto.FileResponse;
import com.example.Miarma.services.StorageService;
import com.example.Miarma.utils.MediaTypeUrlResource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @PostMapping("/upload") //Subir fichero
    public ResponseEntity<FileResponse> upload(@RequestPart("file") MultipartFile file) {

        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        FileResponse archivo = FileResponse.builder()
                .name(name)
                .size(file.getSize())
                .type(file.getContentType())
                .uri(uri)
                .build();

        return ResponseEntity.created(URI.create(uri)).body(archivo);
    }

    @PostMapping("/uploadCustomSize") //Subir fichero tamaño personalizado
    public ResponseEntity<FileResponse> uploadCustom(@RequestPart("file") MultipartFile file, Long size) {

        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        FileResponse archivo = FileResponse.builder()
                .name("esc"+name)
                .size(size)
                .type(file.getContentType())
                .uri(uri)
                .build();

        return ResponseEntity.created(URI.create(uri)).body(archivo);
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        MediaTypeUrlResource resource = (MediaTypeUrlResource) storageService.loadAsResource(filename);


        return ResponseEntity.status(HttpStatus.OK)
                .header("content-type", resource.getType())
                .body(resource);


    }

    @DeleteMapping("/delete/{filename:.+}")
    public ResponseEntity deleteFile(@PathVariable String filename) {
        if(storageService.loadAsResource(filename).exists()) {
            storageService.deleteFile(filename);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

package com.example.Miarma.services;

import com.example.Miarma.config.StorageProperties;
import com.example.Miarma.exception.FileNotFoundException;
import com.example.Miarma.exception.StorageException;
import com.example.Miarma.utils.MediaTypeUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException ex) {
            throw new StorageException("No se ha podido inicializar la localización de almacenamiento", ex);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String newFileName = "";
        try {
            if (file.isEmpty())
                throw new StorageException("El fichero subido está vacío");

            newFileName = filename;
            while (Files.exists(rootLocation.resolve(newFileName))) {
                String extension = StringUtils.getFilenameExtension(newFileName);
                String name = newFileName.replace("." + extension, "");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);

                newFileName = name + "_" + suffix + "." + extension;
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(newFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFileName, ex);
        }
        return newFileName;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException ex) {
            throw new StorageException("Error al leer los ficheros almacenados", ex);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            MediaTypeUrlResource resource = new MediaTypeUrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("No se ha podido leer el archivo " + filename);
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new FileNotFoundException("No se ha podido leer el archivo " + filename, ex);
        }
    }

    @Override
    public void deleteFile(String uri) {
        try {
            String filename = uri.split("/")[4];
            Path file = load(filename);
            Files.delete(file);
        } catch (IOException ex) {
            throw new StorageException("Error al borrar el archivo", ex);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}

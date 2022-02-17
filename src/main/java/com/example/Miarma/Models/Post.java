package com.example.Miarma.Models;

import com.example.Miarma.Dto.FileResponse;
import com.example.Miarma.Utils.MediaTypeUrlResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="posts")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String title;

    private String description;

    private FileResponse archivo;

    private FileResponse escalado;

    private boolean publica = true; //Un contenido sólo para nuestros seguidores

    private UUID idCreador;

    public UUID getIdCreador() {
        return idCreador;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FileResponse getArchivo() {
        return archivo;
    }

    public void setArchivo(FileResponse archivo) {
        this.archivo = archivo;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    public FileResponse getEscalado() {
        return escalado;
    }

    public void setEscalado(FileResponse escalado) {
        this.escalado = escalado;
    }
}

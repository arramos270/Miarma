package com.example.Miarma.image;

public class ImageProcessException extends RuntimeException {

    public ImageProcessException(String mensaje, Exception ex) {
        super(mensaje, ex);
    }
}

package com.example.Miarma.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

public interface ImageScaler {

    public byte[] scale(byte[] image, int width, String type);
    public byte[] scale(byte[] image, int width);

    public OutputStream scale(InputStream inputStream, int width);

    public BufferedImage scale(BufferedImage image, int width);
}

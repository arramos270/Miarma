package com.example.Miarma.utils;

import com.example.Miarma.exception.MediaTypeNotValidException;
import com.example.Miarma.image.ImageScaler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class MediaTypeSelector {

    private final ImageScaler imageScaler;

    public MultipartFile selectMediaType(MultipartFile media) throws Exception {
        if(media.getContentType().contains("image")) {
            return imageScaler.resizePostImage(media);
        } else {
            throw new MediaTypeNotValidException(media.getContentType());
        }
    }
}

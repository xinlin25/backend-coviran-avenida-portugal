package com.example.demo.Proyecto.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String subirImagen(MultipartFile archivo) throws IOException {

        Map subida = cloudinary.uploader().upload(
                archivo.getBytes(),
                ObjectUtils.emptyMap()
        );

        return subida.get("secure_url").toString();
    }
}
package com.kraken.spring_app_coworking.Services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface StorageService {
    String guardarImagen(MultipartFile file) throws IOException,IllegalArgumentException;

    Resource obtenerImagen(String fileName)throws MalformedURLException;

    void setUploadDir(String direccion);

    String getUploadDir();

    void borrarImagen(String rutaImagenBD) throws IOException;
}

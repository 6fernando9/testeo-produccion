package com.kraken.spring_app_coworking.Utils;

import java.util.Arrays;
import java.util.List;

public class ImageUtils {
    private static final List<String> EXTENSIONES = Arrays.asList("png", "jpg", "jpeg");
    private static final Long TAMANIO_MAXIMO_IMG = (long) 50 * 1024 * 1024; // a 50MB
    public static void validarExtension(String nombreArchivo) throws IllegalArgumentException{
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.')+1,nombreArchivo.length()).toLowerCase();
        if(!EXTENSIONES.contains(extension))
            throw new IllegalArgumentException("Error extension no permitida..." + extension) ;

    }

    public static void validarTamanioMaximo(Long size) throws IllegalArgumentException{
        if(size > TAMANIO_MAXIMO_IMG)
            throw new IllegalArgumentException("Error Tamaño de imagen no permitido..." + size) ;
    }
}

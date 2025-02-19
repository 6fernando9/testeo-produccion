package com.kraken.spring_app_coworking.Services.impl;

import com.kraken.spring_app_coworking.Services.interfaces.StorageService;
import com.kraken.spring_app_coworking.Utils.PathsUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.kraken.spring_app_coworking.Utils.ImageUtils.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private String uploadDir;//directorio donde se subira el archivo,seteo desde donde lo necesite el directorio

    /**
     * @param file recibe un archivo que creo que es de cualquier tipo
     * @return
     * @throws IOException
     */
    //        System.out.println(filePathDestino.toAbsolutePath()); //retorna la ruta absoluta
//        System.out.println(filePathDestino.normalize());    //retorna la ruta absoluta y quita los .. o . si pillase
//        System.out.println(filePathDestino.toUri());       //retorna una ruta pero en formato url y es accesible desde la red
    //return filePathDestino.toString(); // retorna la ruta donde se guardo el archivo
    @Override
    public String guardarImagen(MultipartFile file) throws IOException, IllegalArgumentException {
        String fileName = file.getOriginalFilename();// obtenemos el nombre del archivo original
        validarExtension(fileName);
        System.out.println(file.getSize());
        validarTamanioMaximo(file.getSize());
//        System.out.println("guardar imagen: path utils "+ this.uploadDir);
        Path uploadPath = this.getBasePath();//me retorna desde el disco donde se guardo en adelante
//        System.out.println("guardar imagen: upload path"+ uploadPath);
        // creamos el directorio si no existe
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String uuid = UUID.randomUUID().toString();
        // Guardar Archivo
        String nombreArchivo = uuid + "_" + fileName;
        Path filePathDestino = uploadPath.resolve(nombreArchivo);//como concatenar en teoria
        Files.copy(file.getInputStream(), filePathDestino);//copia fisico en el destino (filePath)
        System.out.println("pruebas path destino");
        //System.out.println(Paths.get(PathsUtils.IMAGES_SERVICIO_DIR, filePathDestino.toString()));
        return Paths.get(PathsUtils.IMAGES_SERVICIO_DIR, nombreArchivo).toString();
    }

    /**
     * @param fileName nombre del archivo
     * @return retorna un resourde par visualizar la imagen
     * @throws MalformedURLException
     */
    @Override
    public Resource obtenerImagen(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();//obtengo el path
        Resource resource = new UrlResource(filePath.toUri());//lo transformo a un recurso url

        if (resource.exists() || resource.isReadable())
            return resource;
        throw new RuntimeException("No se pudo cargar el archivo.." + fileName);
    }

    @Override
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public String getUploadDir() {
        return this.uploadDir;
    }

    /**
     * @param rutaImagenBD recibe la ruta directa de la base de datos
     * @throws IOException retorna una excepcion si ocurre algo al eliminar los datos
     */
    @Override
    public void borrarImagen(String rutaImagenBD) throws IOException {
        Path ruta = Paths.get(rutaImagenBD); //obtenemos la ruta completa
        if (Files.exists(ruta)) {
            Files.delete(ruta);
            System.out.println("Imagen Borrada Exitosamente");
        } else {
            System.out.println("Imagen no existe " + rutaImagenBD);
        }
    }

    // Método para obtener la ruta base
    private Path getBasePath() {
        return Paths.get(this.uploadDir).toAbsolutePath().normalize();
    }

    // Método para obtener la ruta de una imagen específica
    private Path getImagePath(String fileName) {
        return getBasePath().resolve(fileName).normalize();
    }


}

package com.BotIntelligent.backend.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try{
            Files.createDirectories(fileStorageLocation);
        } catch (Exception e){
            throw new RuntimeException("Impossible de créer le répertoire de stockage", e);
        }
    }

    public String storeFile(MultipartFile file){
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = "";

        if (originalFilename.contains(".")){
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            if(file.isEmpty()){
                throw new RuntimeException("Fichier vide");
            }

            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")){
                throw new RuntimeException("Seule les images sont acceptées");
            }

            if(file.getSize() > 5*1024*1024){
                throw new RuntimeException("Fichier trop volumineux (max 5Mb)");
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e){
            throw new RuntimeException("Impossible de stocker le fichier");
        }

    }

    public Path loadFile(String filename){
        return this.fileStorageLocation.resolve(filename).normalize();
    }

    public void deleteFile(String filename){
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex){
            throw new RuntimeException("Impossible de supprimer le fichier",ex);
        }
    }

    public @NonNull String getContentType(String fileName) {
        String contentType = "application/octet-stream";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".gif")) {
            contentType = "image/gif";
        } else if (fileName.endsWith(".webp")) {
            contentType = "image/webp";
        }
        return contentType;
    }
}

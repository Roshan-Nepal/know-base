package com.roshan.know_base.document.service;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService{
    private final Path rootLocation;
    public LocalStorageService(@Value("${knwo-base.storage.local.path:./storage}") String path){
        this.rootLocation = Paths.get(path);
    }

    @PostConstruct
    public void init(){
        try{
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new DocumentProcessingException("Could not initialize storage directory",
                    ErrorCode.DOCUMENT_STORAGE_ERROR,
                    HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public String store(MultipartFile file, UUID documentId) {
        if(file.isEmpty()){
            throw new DocumentProcessingException("Failed to store empty file.", ErrorCode.DOCUMENT_STORAGE_ERROR, HttpStatus.BAD_REQUEST);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String storage = documentId.toString() + extension;

        Path destinationFile = this.rootLocation.resolve(Paths.get(storage))
                .normalize().toAbsolutePath();
        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DocumentProcessingException("Failed to store file.",
                    ErrorCode.DOCUMENT_STORAGE_ERROR,
                    HttpStatus.BAD_REQUEST );
        }
        return storage;
    }

    @Override
    public InputStream loadAsResource(String storage) {
        try{
            Path file = rootLocation.resolve(storage);
            if(Files.exists(file) && Files.isReadable(file)){
                return Files.newInputStream(file);
            }
            throw new DocumentProcessingException("Failed to store file.",
                    ErrorCode.DOCUMENT_PROCESSING_FAILED,
                    HttpStatus.UNPROCESSABLE_CONTENT );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

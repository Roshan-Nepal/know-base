package com.roshan.know_base.document.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {
    String store(MultipartFile file, UUID documentId);

    InputStream loadAsResource(String storageKey);
}

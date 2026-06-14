package com.roshan.know_base.document.service;

import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface DocumentService {
    DocumentResponse uploadDocument(MultipartFile file);

    DocumentResponse get(UUID id);
}

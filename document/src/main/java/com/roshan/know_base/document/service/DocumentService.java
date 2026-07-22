package com.roshan.know_base.document.service;

import com.roshan.know_base.document.dto.DocumentResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    DocumentResponse uploadDocument(MultipartFile file, List<String> tags);

    DocumentResponse get(UUID id);

    Page<DocumentResponse> getAll(int pageNumber, int size);

    void delete(UUID id);
}

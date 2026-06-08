package com.roshan.know_base.document.controller;

import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.entity.Document;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import com.roshan.know_base.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new DocumentProcessingException("Cannot upload an empty file.", ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        return ApiResponseHelper.successResponse(documentService.uploadDocument(file), "Document Uploaded Successfully.", HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@PathVariable UUID id) {

        return ApiResponseHelper.successResponse(documentService.get(id), "Ok.");

    }
}

package com.roshan.know_base.document.controller;

import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.dto.PageResponse;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import com.roshan.know_base.document.dto.DocumentDetailResponse;
import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import com.roshan.know_base.document.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "APIs for managing documents")
public class DocumentController {
    private final DocumentService documentService;


    @Operation(
            summary = "Upload a document",
            description = "Uploads a document with optional tags."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tags", required = false) List<String> tags
            ) {

        if (file.isEmpty()) {
            throw new DocumentProcessingException("Cannot upload an empty file.", ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
        return ApiResponseHelper.successResponse(documentService.uploadDocument(file,tags), "Document Uploaded Successfully.", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get document by ID",
            description = "Retrieves a document by its unique identifier."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentDetailResponse>> getDocument(@PathVariable UUID id) {

        return ApiResponseHelper.successResponse(documentService.get(id), "Ok.");

    }

    @Operation(
            summary = "List documents",
            description = "Returns a paginated list of documents."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DocumentResponse>>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size){
        return ApiResponseHelper.pageResponse(documentService.getAll(pageNumber, size), "Document Fetched Successfully.");
    }

    @Operation(
            summary = "Delete a document",
            description = "Soft deletes a document by its ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        documentService.delete(id);
        return ApiResponseHelper.successResponse("Document deleted successfully.");
    }
}

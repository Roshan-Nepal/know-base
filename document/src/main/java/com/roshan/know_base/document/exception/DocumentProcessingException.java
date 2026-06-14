package com.roshan.know_base.document.exception;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DocumentProcessingException extends BaseException {
    public DocumentProcessingException(String message, ErrorCode errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}

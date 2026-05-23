package com.roshan.know_base.common.exception;

import com.roshan.know_base.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String message, ErrorCode errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}

package com.roshan.know_base.common.exception;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(String message, ErrorCode errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}

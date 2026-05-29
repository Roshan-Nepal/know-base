package com.roshan.know_base.common.exception;

import com.roshan.know_base.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyExistException extends BaseException{
    public AlreadyExistException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}

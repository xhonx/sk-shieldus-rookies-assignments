package com.rookies6.myspringbootlab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
    private HttpStatus httpStatus;

    public BusinessException(String message) {
        //417
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    //ErrorCode 의 메시지 템플릿에 args 를 채워 메시지를 만든다
    public BusinessException(ErrorCode errorCode, Object... args) {
        this.message = errorCode.formatMessage(args);
        this.httpStatus = errorCode.getHttpStatus();
    }
}

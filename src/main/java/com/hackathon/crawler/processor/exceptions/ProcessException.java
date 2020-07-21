package com.hackathon.crawler.processor.exceptions;

public class ProcessException extends BaseException {

    public ProcessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}

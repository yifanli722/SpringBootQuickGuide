package com.SpringQuickGuide.ImageCrud.service;

import org.springframework.http.HttpStatus;

public class Result<T> {
    T result;
    String errorMsg;
    HttpStatus responseCode;

    Result(T result, String errorMsg, HttpStatus responseCode) {
        this.result = result;
        this.errorMsg = errorMsg;
        this.responseCode = responseCode;
    }

    public T getResult() {
        return result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public HttpStatus getStatusCode() {
        return responseCode;
    }
}

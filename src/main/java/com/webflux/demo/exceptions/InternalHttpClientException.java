package com.webflux.demo.exceptions;

import lombok.Getter;

@Getter
public class InternalHttpClientException extends RuntimeException {
    private final int code;
    private final String url;
    private final Object request;
    private final Object response;

    public InternalHttpClientException(String message, int code, String url, Object request, Object response) {
        super(message);
        this.url = url;
        this.code = code;
        this.request = request;
        this.response = response;
    }
    public <T> T getResponse(Class<T> clazz) {
        return clazz.cast(response);
    }
    public <T> T getRequest(Class<T> clazz) {
        return clazz.cast(request);
    }
}


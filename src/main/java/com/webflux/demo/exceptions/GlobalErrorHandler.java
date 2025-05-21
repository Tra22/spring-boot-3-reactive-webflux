package com.webflux.demo.exceptions;

import com.webflux.demo.payloads.clients.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(InternalHttpClientException.class)
    public Mono<ResponseEntity<ApiResponse<?>>> handle(InternalHttpClientException ex) {
        return Mono.just(ResponseEntity
                .status(ex.getCode())
                .body(ApiResponse.error(ex.getCode(), ex.getMessage())));
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public Mono<ResponseEntity<ApiResponse<?>>> handle(Exception ex) {
        return Mono.just(ResponseEntity
                .status(500)
                .body(ApiResponse.error(500, ex.getMessage(), ex)));
    }
}
package com.webflux.demo.payloads.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class ApiResponse<T> implements Serializable {
    private int code = 200;
    private String message;
    private Metadata metadata;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("Success")
                .metadata(Metadata.successBlock())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .metadata(Metadata.errorBlock(null))
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message, Throwable e) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .metadata(Metadata.errorBlock(e))
                .build();
    }
}

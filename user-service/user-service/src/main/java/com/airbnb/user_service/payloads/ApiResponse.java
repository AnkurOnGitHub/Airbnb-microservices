package com.airbnb.user_service.payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T>{
    private String message;
    private boolean success;
    private T data;

    public static<T> ApiResponse<T>success(T data){
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .build();
    }
    public static<T> ApiResponse<T>success(T data, String message){
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .success(true)
                .build();
    }
    public static<T> ApiResponse<T>success(String message){
        return ApiResponse.<T>builder()
                .message(message)
                .success(true)
                .build();
    }
    public static<T> ApiResponse<T>error(T data){
        return ApiResponse.<T>builder()
                .data(data)
                .success(false)
                .build();
    }
    public static<T> ApiResponse<T>error(T data, String message){
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .success(false)
                .build();
    }
    public static<T> ApiResponse<T>error(String message){
        return ApiResponse.<T>builder()
                .message(message)
                .success(false)
                .build();
    }
}

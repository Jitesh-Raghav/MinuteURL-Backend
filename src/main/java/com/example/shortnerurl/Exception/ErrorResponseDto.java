package com.example.shortnerurl.Exception;

import lombok.Data;

@Data
public class ErrorResponseDto {

    private String status;
    private String error;
}

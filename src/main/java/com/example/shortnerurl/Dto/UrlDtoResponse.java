package com.example.shortnerurl.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlDtoResponse {
    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}

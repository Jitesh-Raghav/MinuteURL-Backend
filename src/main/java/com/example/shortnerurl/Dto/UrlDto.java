package com.example.shortnerurl.Dto;

import lombok.*;

@Setter@Getter
@AllArgsConstructor@NoArgsConstructor
@Data
public class UrlDto {
    private String url;
    private String expirationDate;  //optional

}

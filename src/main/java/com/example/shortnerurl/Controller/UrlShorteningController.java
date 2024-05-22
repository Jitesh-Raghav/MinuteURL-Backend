package com.example.shortnerurl.Controller;

import com.example.shortnerurl.Dto.UrlDto;
import com.example.shortnerurl.Dto.UrlDtoResponse;
import com.example.shortnerurl.Entity.Url;
import com.example.shortnerurl.Exception.ErrorResponseDto;
import com.example.shortnerurl.Service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = {"http://127.0.0.1:5501"})
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;

    public UrlShorteningController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {

        Url urlToRet = urlService.generateShortLink(urlDto);

        //THIS IS DONE CUZ WE WANNA RETURN UrlResponseDto OBJECT, WE COULD HAVE
        //RETURNED urlToRet HERE, BUT WE DON'T WANNA RETURN URL ENTITY, WE WANT TO RETURN URLRESPONSEDTO ENTITY...

        if (urlToRet != null) {

            UrlDtoResponse urlDtoResponse = new UrlDtoResponse();
            urlDtoResponse.setOriginalUrl(urlToRet.getOriginalUrl());
            urlDtoResponse.setExpirationDate(urlToRet.getExpirationDate());
            urlDtoResponse.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<>(urlDtoResponse, HttpStatus.OK);
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus("404");
        errorResponseDto.setError("There was an error processing your request. Please try again.");
        return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.OK);

    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        if (StringUtils.isEmpty(shortLink)) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setError("invalid url");
            errorResponseDto.setStatus("400");
            return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if(urlToRet==null) {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setError("Url doesn't exist or might have expired");
            errorResponseDto.setStatus("400");
            return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.OK);
        }

        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {

            urlService.deleteShortLink(urlToRet);
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setError("Url has expired, try generating a new one");
            errorResponseDto.setStatus("200");
            return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());

        return null;
    }

    @GetMapping("/getall")
    public List<Url> getAllUrl(){
        List<Url> urlList= urlService.findAllByOrderByCreatedAtDesc();
        return urlList;
    }

}

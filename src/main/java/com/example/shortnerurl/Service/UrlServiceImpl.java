package com.example.shortnerurl.Service;

import com.example.shortnerurl.Dto.UrlDto;
import com.example.shortnerurl.Entity.Url;
import com.example.shortnerurl.Repository.UrlRepo;
import com.google.common.hash.Hashing;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class UrlServiceImpl implements UrlService{

    @Autowired
    private UrlRepo urlRepo;

    public UrlServiceImpl(UrlRepo urlRepo) {
        this.urlRepo = urlRepo;
    }

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(StringUtils.isNotEmpty(urlDto.getUrl())) {

            String encodedUrl = encodedUrl(urlDto.getUrl());
            Url urlToPersist= new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
            Url urlToReturn = persistShortLink(urlToPersist);

            if(urlToReturn!=null) return urlToReturn;

            return null;
        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {

        if(StringUtils.isBlank((expirationDate))){
            return creationDate.plusSeconds(60);
        }
        LocalDateTime expirationDateToReturn = LocalDateTime.parse(expirationDate);
        return  expirationDateToReturn;

    }

    private String encodedUrl(String url) {

        String encodedUrl="";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl= Hashing.murmur3_32_fixed()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();

        return encodedUrl;

    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToReturn = urlRepo.save(url);
        return urlToReturn;
    }

    @Override
    public Url getEncodedUrl(String url) {
       Url urlToReturn = urlRepo.findByShortLink(url);
       return urlToReturn;
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepo.delete(url);
    }

    @Override
    public List<Url> findAllByOrderByCreatedAtDesc(){
        return urlRepo.findAll();
    }
}
